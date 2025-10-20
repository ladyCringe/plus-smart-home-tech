package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.sensor.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

@Component
public class LightSensorEventMapper implements SensorEventProtoMapper {

    @Override
    public SensorEventProto.PayloadCase key() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public SensorEvent map(SensorEventProto p) {
        LightSensorEvent e = new LightSensorEvent();

        SensorBaseMapper.fillBase(e, p);
        e.setLinkQuality(p.getLightSensorEvent().getLinkQuality());
        e.setLuminosity(p.getLightSensorEvent().getLuminosity());
        return e;
    }
}
