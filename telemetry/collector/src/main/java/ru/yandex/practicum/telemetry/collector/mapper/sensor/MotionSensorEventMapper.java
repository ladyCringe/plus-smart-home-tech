package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

public class MotionSensorEventMapper implements SensorEventProtoMapper {

    @Override
    public SensorEventProto.PayloadCase key() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public SensorEvent map(SensorEventProto p) {
        MotionSensorEvent e = new MotionSensorEvent();

        SensorBaseMapper.fillBase(e, p);
        e.setLinkQuality(p.getMotionSensorEvent().getLinkQuality());
        e.setMotion(p.getMotionSensorEvent().getMotion());
        e.setVoltage(p.getMotionSensorEvent().getVoltage());
        return e;
    }
}
