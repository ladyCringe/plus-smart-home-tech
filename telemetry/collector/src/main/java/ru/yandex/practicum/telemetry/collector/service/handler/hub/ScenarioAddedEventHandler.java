package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceAction;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioCondition;
import ru.yandex.practicum.telemetry.collector.service.handler.EventHandler;

import java.util.List;

@Component
public class ScenarioAddedEventHandler extends EventHandler<ScenarioAddedEvent, HubEventAvro, HubEventType> {
    @Override
    public HubEventAvro handle(ScenarioAddedEvent event) {
        List<ScenarioConditionAvro> newConditions = event.getConditions().stream()
                .map(this::mapConditions)
                .toList();

        List<DeviceActionAvro> newAction = event.getActions().stream()
                .map(this::mapActions)
                .toList();

        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setConditions(newConditions)
                .setActions(newAction)
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private ScenarioConditionAvro mapConditions(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().toString()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().toString()))
                .setValue(condition.getValue())
                .build();
    }

    private DeviceActionAvro mapActions(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                .setValue(action.getValue())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
