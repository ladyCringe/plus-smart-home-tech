package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

public class MotionSensorEventMapper implements SensorEventProtoMapper {

    @Override
    public SensorEventProto.PayloadCase key() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    public SensorEvent map(SensorEventProto p) {
        MotionSensorEvent e = new MotionSensorEvent();

        SensorBaseMapper.fillBase(e, p);
        e.setLinkQuality(p.getMotionSensor().getLinkQuality());
        e.setMotion(p.getMotionSensor().getMotion());
        e.setVoltage(p.getMotionSensor().getVoltage());
        return e;
    }
}
