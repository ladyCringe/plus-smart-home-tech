package ru.yandex.practicum.telemetry.collector.mapper.hub;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

@Component
public class DeviceAddedEventMapper implements HubEventProtoMapper {

    @Override
    public HubEventProto.PayloadCase key() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public HubEvent map(HubEventProto p) {
        DeviceAddedEvent e = new DeviceAddedEvent();

        HubBaseMapper.fillBase(e, p);
        e.setId(p.getDeviceAdded().getId());
        e.setDeviceType(p.getDeviceAdded().getType().name());
        return e;
    }
}

