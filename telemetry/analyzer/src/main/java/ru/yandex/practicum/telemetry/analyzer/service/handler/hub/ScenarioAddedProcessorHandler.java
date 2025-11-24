package ru.yandex.practicum.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.core.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.analyzer.domain.model.*;
import ru.yandex.practicum.telemetry.analyzer.domain.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.domain.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.domain.repository.ScenarioRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedProcessorHandler implements HubEventHandler<ScenarioAddedEventAvro> {

    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Override
    public Class payloadType() {
        return ScenarioAddedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        ScenarioAddedEventAvro scenarioAddedEventAvro = (ScenarioAddedEventAvro) event.getPayload();
        Scenario scenario = Scenario.builder()
                .hubId(event.getHubId())
                .name(scenarioAddedEventAvro.getName())
                .conditions(new HashMap<>())
                .actions(new HashMap<>())
                .build();
        scenarioRepository.findByHubIdAndName(scenario.getHubId(), scenario.getName()).ifPresentOrElse(
                s -> {
                    log.info("Scenario {} already exists", s.getName());
                },
                () -> {
                    log.debug("Creating scenario: {}", scenario.getName());
                    List<Condition> conditions = scenarioAddedEventAvro.getConditions().stream()
                            .map(this::toCondition)
                            .peek(c -> scenario.addCondition(c.getSensorId(), c))
                            .toList();
                    log.debug("List of conditions: {}", conditions);
                    List<Condition> newConditions = conditionRepository.saveAll(conditions);
                    log.trace("list of conditions {} for Scenario {} was added", newConditions, scenario.getName());

                    List<Action> actions = scenarioAddedEventAvro.getActions().stream()
                            .map(this::toAction)
                            .peek(a -> scenario.addAction(a.getSensorId(), a))
                            .toList();
                    log.debug("list of actions {}", actions);
                    List<Action> newActions = actionRepository.saveAll(actions);
                    log.trace("List of actions {} for Scenario {} was added", newActions, scenario.getName());

                    Scenario newScenario = scenarioRepository.save(scenario);
                    log.debug("list of conditions {} for Scenario {}", newScenario.getConditions(), newScenario.getName());
                    log.debug("List of actions {} for Scenario {}", newScenario.getActions(), newScenario.getName());

                    log.info("Scenario {} was created", newScenario.getName());
                });
    }

    private Condition toCondition(ScenarioConditionAvro scenarioConditionAvro) {
        Condition condition = Condition.builder()
                .type(ConditionType.valueOf(scenarioConditionAvro.getType().toString()))
                .operation(ConditionOperation.valueOf(scenarioConditionAvro.getOperation().toString()))
                .sensorId(scenarioConditionAvro.getSensorId())
                .build();
        Object value = scenarioConditionAvro.getValue();
        if (Objects.nonNull(value)) {
            if (value instanceof Integer) condition.setValue((Integer) value);
            if (value instanceof Boolean) condition.setValue((Boolean) value ? 1 : 0);
        } else {
            condition.setValue(null);
        }
        return condition;
    }

    private Action toAction(DeviceActionAvro deviceActionAvro) {
        return Action.builder()
                .type(deviceActionAvro.getType().toString())
                .value((deviceActionAvro.getValue() != null)
                        ? deviceActionAvro.getValue() : null)
                .sensorId(deviceActionAvro.getSensorId())
                .build();
    }

}
