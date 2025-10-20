package ru.yandex.practicum.telemetry.collector.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.mapper.hub.HubEventProtoMapper;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class HubEventMapper {
    private final Map<HubEventProto.PayloadCase, HubEventProtoMapper> mapper;

    public HubEventMapper(List<HubEventProtoMapper> mappers) {
        this.mapper = new EnumMap<>(HubEventProto.PayloadCase.class);
        for (var m : mappers) {
            this.mapper.put(m.key(), m);
        }
    }

    public HubEvent map(HubEventProto proto) {
        var mappingClass = mapper.get(proto.getPayloadCase());
        if (mappingClass == null) {
            throw new IllegalArgumentException("No mapper for payload: " + proto.getPayloadCase());
        }
        return mappingClass.map(proto);
    }
}
