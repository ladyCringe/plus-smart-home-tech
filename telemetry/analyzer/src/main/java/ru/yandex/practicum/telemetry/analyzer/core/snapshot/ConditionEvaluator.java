package ru.yandex.practicum.telemetry.analyzer.core.snapshot;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.analyzer.domain.model.Condition;
import java.util.Objects;

@Component
public class ConditionEvaluator {
  public boolean test(int sensorValue, Condition c) {
    return switch (c.getOperation()) {
      case EQUALS -> Objects.equals(sensorValue, c.getValue());
      case GREATER_THAN -> sensorValue > c.getValue();
      case LOWER_THAN -> sensorValue < c.getValue();
    };
  }
}
