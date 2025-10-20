package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.TemperatureSensorEvent;

public class TemperatureSensorEventMapper implements SensorEventProtoMapper {

    @Override
    public SensorEventProto.PayloadCase key() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    public SensorEvent map(SensorEventProto p) {
        TemperatureSensorEvent e = new TemperatureSensorEvent();

        SensorBaseMapper.fillBase(e, p);
        e.setTemperatureC(p.getTemperatureSensor().getTemperatureC());
        e.setTemperatureF(p.getTemperatureSensor().getTemperatureF());
        return e;
    }
}
