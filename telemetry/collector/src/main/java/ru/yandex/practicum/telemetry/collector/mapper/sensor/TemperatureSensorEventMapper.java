package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.TemperatureSensorEvent;

@Component
public class TemperatureSensorEventMapper implements SensorEventProtoMapper {

    @Override
    public SensorEventProto.PayloadCase key() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public SensorEvent map(SensorEventProto p) {
        TemperatureSensorEvent e = new TemperatureSensorEvent();

        SensorBaseMapper.fillBase(e, p);
        e.setTemperatureC(p.getTemperatureSensorEvent().getTemperatureC());
        e.setTemperatureF(p.getTemperatureSensorEvent().getTemperatureF());
        return e;
    }
}
