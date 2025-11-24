package ru.yandex.practicum.telemetry.collector.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.mapper.sensor.SensorEventProtoMapper;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class SensorEventMapper {
    private final Map<SensorEventProto.PayloadCase, SensorEventProtoMapper> mapper;

    public SensorEventMapper(List<SensorEventProtoMapper> mappers) {
        this.mapper = new EnumMap<>(SensorEventProto.PayloadCase.class);
        for (var m : mappers) {
            this.mapper.put(m.key(), m);
        }
    }

    public SensorEvent map(SensorEventProto proto) {
        var mappingClass = mapper.get(proto.getPayloadCase());
        if (mappingClass == null) {
            throw new IllegalArgumentException("No mapper for payload: " + proto.getPayloadCase());
        }
        return mappingClass.map(proto);
    }
}
