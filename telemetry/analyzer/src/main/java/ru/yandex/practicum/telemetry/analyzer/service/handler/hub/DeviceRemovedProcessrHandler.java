package ru.yandex.practicum.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.core.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.analyzer.domain.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.domain.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRemovedProcessrHandler implements HubEventHandler<DeviceRemovedEventAvro> {

    private final SensorRepository sensorRepository;

    @Override
    public Class payloadType() {
        return DeviceRemovedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro deviceRemovedEventAvro = (DeviceRemovedEventAvro) event.getPayload();
        Sensor sensor = Sensor.builder()
                .id(deviceRemovedEventAvro.getId())
                .hubId(event.getHubId())
                .build();
        sensorRepository.findByIdAndHubId(sensor.getId(), sensor.getHubId()).ifPresentOrElse(
                d -> {
                    sensorRepository.delete(d);
                    log.info("Device was deleted {}", d);
                }, () -> log.info("Device wasn't found {}", sensor));
    }
}
