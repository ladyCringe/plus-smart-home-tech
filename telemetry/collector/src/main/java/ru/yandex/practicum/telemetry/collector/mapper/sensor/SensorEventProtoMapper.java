package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

public interface SensorEventProtoMapper {

    SensorEventProto.PayloadCase key();

    SensorEvent map(SensorEventProto proto);

}


