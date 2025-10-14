package ru.yandex.practicum.telemetry.collector.service;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.telemetry.collector.config.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.EventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.yandex.practicum.telemetry.collector.utils.TypeUtils.getEventTypeFromHandler;

@Slf4j
@Service
public class HubEventService {
    private final KafkaEventProducer kafkaProducer;
    private final Map<HubEventType, EventHandler<?, ?, ?>> hubEventHandlers;

    public HubEventService(final Set<EventHandler<?, ?, ?>> eventHandlers, final KafkaEventProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        this.hubEventHandlers = eventHandlers.stream()
                .filter(handler -> handler.getMessageType() instanceof HubEventType)
                .collect(Collectors.toMap(
                        handler -> (HubEventType) handler.getMessageType(),
                        Function.identity()
                ));
    }

    @Value("${kafka.topics.hub-events:telemetry.hubs.v1}")
    private String hubEventTopic;

    @SuppressWarnings("unchecked")
    public void collectHubEvent(@Validated @RequestBody HubEvent event) {
        log.info("Collecting sensor event: {}", event);

        EventHandler handler = hubEventHandlers.get(event.getType());
        if (handler == null) {
            throw new ValidationException("Unknown hub event type: " + event.getType());
        }

        validateType(event, getEventTypeFromHandler(handler));
        handler.handleEvent(kafkaProducer, hubEventTopic, event.getHubId(), event);
    }

    private void validateType(HubEvent event, Class<?> expected) {
        if (!expected.isInstance(event)) {
            throw new ValidationException(event.getClass() + " is not of expected type: " + expected.getName());
        }
    }
}
