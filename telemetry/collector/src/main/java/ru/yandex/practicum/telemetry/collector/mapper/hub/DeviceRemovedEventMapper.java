package ru.yandex.practicum.telemetry.collector.mapper.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

@Component
public class DeviceRemovedEventMapper implements HubEventProtoMapper {

    @Override
    public HubEventProto.PayloadCase key() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public HubEvent map(HubEventProto p) {
        DeviceRemovedEvent e = new DeviceRemovedEvent();

        HubBaseMapper.fillBase(e, p);
        e.setId(p.getDeviceRemoved().getId());
        return e;
    }
}
