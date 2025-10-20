package ru.yandex.practicum.telemetry.collector.mapper.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioAddedEvent;

public class ScenarioRemovedEventMapper implements HubEventProtoMapper {

    @Override
    public HubEventProto.PayloadCase key() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public HubEvent map(HubEventProto p) {
        ScenarioAddedEvent e = new ScenarioAddedEvent();

        HubBaseMapper.fillBase(e, p);
        e.setName(p.getScenarioAdded().getName());
        return e;
    }
}
