package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.EventHandler;

@Component
public class DeviceRemovedEventHandler extends EventHandler<DeviceRemovedEvent, HubEventAvro, HubEventType> {
    @Override
    public HubEventAvro handle(DeviceRemovedEvent event) {
        DeviceRemovedEventAvro payload = DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
