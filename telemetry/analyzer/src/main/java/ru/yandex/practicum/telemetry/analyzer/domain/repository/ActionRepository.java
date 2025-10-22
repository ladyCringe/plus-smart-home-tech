package ru.yandex.practicum.telemetry.analyzer.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.analyzer.domain.model.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
}