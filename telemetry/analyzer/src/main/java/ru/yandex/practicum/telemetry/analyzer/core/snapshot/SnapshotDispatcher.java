package ru.yandex.practicum.telemetry.analyzer.core.snapshot;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.domain.model.*;
import ru.yandex.practicum.telemetry.analyzer.domain.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.gateway.HubRouterClient;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SnapshotDispatcher {

  private final ScenarioRepository scenarios;
  private final ConditionEvaluator evaluator;
  private final HubRouterClient hub;

  public void dispatch(Object rec) {
    if (!(rec instanceof SensorsSnapshotAvro snap)) return;

    String hubId = snap.getHubId();
    Map<String, SensorStateAvro> st = snap.getSensorsState();
    List<Scenario> list = scenarios.findByHubId(hubId);
    if (list.isEmpty()) return;

    list.stream()
      .filter(sc -> st.keySet().containsAll(sc.getConditions().keySet()))
      .filter(sc -> sc.getConditions().entrySet().stream()
         .allMatch(e -> check(st.get(e.getKey()), e.getValue())))
      .forEach(this::send);
  }

  private boolean check(SensorStateAvro ss, Condition c) {
    Object p = ss.getData();
    Integer v = switch (c.getType()) {
      case TEMPERATURE -> (p instanceof TemperatureSensorAvro t) ? t.getTemperatureC()
                             : (p instanceof ClimateSensorAvro cl) ? cl.getTemperatureC() : null;
      case HUMIDITY -> (p instanceof ClimateSensorAvro cl) ? cl.getHumidity() : null;
      case CO2LEVEL -> (p instanceof ClimateSensorAvro cl) ? cl.getCo2Level() : null;
      case LUMINOSITY -> (p instanceof LightSensorAvro l) ? l.getLuminosity() : null;
      case MOTION -> (p instanceof MotionSensorAvro m) ? (m.getMotion() ? 1 : 0) : null;
      case SWITCH -> (p instanceof SwitchSensorAvro s) ? (s.getState() ? 1 : 0) : null;
    };
    return v != null && evaluator.test(v, c);
  }

  private void send(Scenario sc) {
    sc.getActions().forEach((sensorId, a) -> {
      var action = DeviceActionProto.newBuilder()
        .setSensorId(sensorId)
        .setType(ActionTypeProto.valueOf(a.getType().trim().toUpperCase()))
        .setValue(a.getValue() == null ? 0 : a.getValue())
        .build();
      var now = Instant.now();
      var req = DeviceActionRequestProto.newBuilder()
        .setHubId(sc.getHubId())
        .setScenarioName(sc.getName())
        .setAction(action)
        .setTimestamp(Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()))
        .build();
      hub.send(req);
    });
  }
}
