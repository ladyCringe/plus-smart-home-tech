package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.sensor.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

public class LightSensorEventMapper implements SensorEventProtoMapper {

    @Override
    public SensorEventProto.PayloadCase key() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }

    @Override
    public SensorEvent map(SensorEventProto p) {
        LightSensorEvent e = new LightSensorEvent();

        SensorBaseMapper.fillBase(e, p);
        e.setLinkQuality(p.getLightSensor().getLinkQuality());
        e.setLuminosity(p.getLightSensor().getLuminosity());
        return e;
    }
}
