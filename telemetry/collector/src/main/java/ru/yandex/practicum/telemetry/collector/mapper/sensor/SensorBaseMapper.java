package ru.yandex.practicum.telemetry.collector.mapper.sensor;


import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

import java.time.Instant;

public class SensorBaseMapper {
    public static void fillBase(SensorEvent e, SensorEventProto p) {
        e.setId(p.getId());
        e.setHubId(p.getHubId());
        e.setTimestamp(Instant.ofEpochSecond(
                p.getTimestamp().getSeconds(),
                p.getTimestamp().getNanos()
        ));
    }
}
