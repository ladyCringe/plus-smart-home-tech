package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SwitchSensorEvent;

public class SwitchSensorEventMapper implements SensorEventProtoMapper {

    @Override
    public SensorEventProto.PayloadCase key() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    public SensorEvent map(SensorEventProto p) {
        SwitchSensorEvent e = new SwitchSensorEvent();

        SensorBaseMapper.fillBase(e, p);
        e.setState(p.getSwitchSensor().getState());
        return e;
    }
}
