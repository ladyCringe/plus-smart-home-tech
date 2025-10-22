package ru.yandex.practicum.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.analyzer.core.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.analyzer.domain.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.domain.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.domain.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.domain.repository.ScenarioRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioRemovedProcesorHandler implements HubEventHandler<ScenarioRemovedEventAvro> {

    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Override
    public Class payloadType() {
        return ScenarioRemovedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        ScenarioRemovedEventAvro scenarioRemovedEventAvro = (ScenarioRemovedEventAvro) event.getPayload();
        Scenario scenario = Scenario.builder()
                .hubId(event.getHubId())
                .name(scenarioRemovedEventAvro.getName())
                .build();
        scenarioRepository.findByHubIdAndName(scenario.getHubId(), scenario.getName()).ifPresentOrElse(
                s -> {
                    scenarioRepository.delete(s);
                    conditionRepository.deleteAll(s.getConditions().values().stream().toList());
                    actionRepository.deleteAll(s.getActions().values().stream().toList());

                    log.info("Deleting scenario {}", s.getName());
                },
                () -> log.info("Scenario {} was not found", scenario.getName()));

    }
}
