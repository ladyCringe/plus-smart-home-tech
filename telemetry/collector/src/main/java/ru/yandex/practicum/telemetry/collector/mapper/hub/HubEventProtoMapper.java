package ru.yandex.practicum.telemetry.collector.mapper.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

public interface HubEventProtoMapper {
    HubEventProto.PayloadCase key();
    HubEvent map(HubEventProto proto);
}
