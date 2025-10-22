package ru.yandex.practicum.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.core.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.analyzer.domain.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.domain.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedProcessorHandler implements HubEventHandler<DeviceAddedEventAvro> {

    private final SensorRepository sensorRepository;

    @Override
    public Class payloadType() {
        return DeviceAddedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceAddedEventAvro deviceAddedEventAvro = (DeviceAddedEventAvro) event.getPayload();
        Sensor sensor = Sensor.builder()
                .id(deviceAddedEventAvro.getId())
                .hubId(event.getHubId())
                .build();
        sensorRepository.findByIdAndHubId(sensor.getId(), sensor.getHubId()).ifPresentOrElse(
                d -> log.info("Device already exists: {}", d),
                () -> {
                    sensorRepository.save(sensor);
                    log.info("Device activated {}", sensor);
                });
    }
}
