package ru.yandex.practicum.telemetry.collector.service;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.collector.config.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.EventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.yandex.practicum.telemetry.collector.utils.TypeUtils.getEventTypeFromHandler;

@Slf4j
@Service
public class SensorEventService {
    private final KafkaEventProducer kafkaProducer;
    private final Map<SensorEventType, EventHandler<?, ?, ?>> sensorEventHandlers;

    public SensorEventService(final Set<EventHandler<?, ?, ?>> eventHandlers, final KafkaEventProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        this.sensorEventHandlers = eventHandlers.stream()
                .filter(handler -> handler.getMessageType() instanceof SensorEventType)
                .collect(Collectors.toMap(
                        handler -> (SensorEventType) handler.getMessageType(),
                        Function.identity()
                ));
    }

    @Value("${kafka.topics.sensor-events:telemetry.sensors.v1}")
    private String sensorEventTopic;

    @SuppressWarnings("unchecked")
    public void collectSensorEvent(SensorEvent event) {
        log.info("Collecting sensor event: {}", event);

        EventHandler handler = sensorEventHandlers.get(event.getType());
        if (handler == null) {
            throw new ValidationException("Unknown sensor event type: " + event.getType());
        }

        validateType(event, getEventTypeFromHandler(handler));
        handler.handleEvent(kafkaProducer, sensorEventTopic, event.getHubId(), event);
    }

    private void validateType(SensorEvent event, Class<?> expected) {
        if (!expected.isInstance(event)) {
            throw new ValidationException(event.getClass() + " is not of expected type: " + expected.getName());
        }
    }

}
