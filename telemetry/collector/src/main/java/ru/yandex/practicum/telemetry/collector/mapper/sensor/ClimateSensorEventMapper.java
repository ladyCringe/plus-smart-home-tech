package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

public class ClimateSensorEventMapper implements SensorEventProtoMapper {

    @Override
    public SensorEventProto.PayloadCase key() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public SensorEvent map(SensorEventProto p) {
        ClimateSensorEvent e = new ClimateSensorEvent();

        SensorBaseMapper.fillBase(e, p);
        e.setTemperatureC(p.getClimateSensorEvent().getTemperatureC());
        e.setHumidity(p.getClimateSensorEvent().getHumidity());
        e.setCo2Level(p.getClimateSensorEvent().getCo2Level());
        return e;
    }
}
