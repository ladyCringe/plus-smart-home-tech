package ru.yandex.practicum.telemetry.collector.mapper.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

import java.time.Instant;

public class HubBaseMapper {
    public static void fillBase(HubEvent e, HubEventProto p) {
        e.setHubId(p.getHubId());
        e.setTimestamp(Instant.ofEpochSecond(
                p.getTimestamp().getSeconds(),
                p.getTimestamp().getNanos()
        ));
    }
}
