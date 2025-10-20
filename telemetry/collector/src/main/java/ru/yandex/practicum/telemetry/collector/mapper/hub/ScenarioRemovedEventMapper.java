package ru.yandex.practicum.telemetry.collector.mapper.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.telemetry.collector.model.hub.*;

import java.util.stream.Collectors;

@Component
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
        e.setConditions(p.getScenarioAdded().getConditionList().stream()
                .map(this::mapCondition)
                .collect(Collectors.toList()));
        e.setActions(p.getScenarioAdded().getActionList().stream()
                .map(this::mapActions)
                .collect(Collectors.toList()));
        return e;
    }

    private ScenarioCondition mapCondition(ScenarioConditionProto c) {
        var result = new ScenarioCondition();
        result.setSensorId(c.getSensorId());
        result.setType(ConditionType.valueOf(c.getType().name()));
        result.setOperation(ConditionOperation.valueOf(c.getOperation().name()));
        result.setValue(c.getValueCase().getNumber() == 4 ? c.getBoolValue() ? 1 : 0 : c.getIntValue());
        return result;
    }

    private DeviceAction mapActions(DeviceActionProto c) {
        var result = new DeviceAction();
        result.setSensorId(c.getSensorId());
        result.setType(ActionType.valueOf(c.getType().name()));
        result.setValue(c.getValue());
        return result;
    }
}
