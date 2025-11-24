package ru.yandex.practicum.telemetry.analyzer.core.hub;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler<T> {
  Class<T> payloadType();

  void handle(HubEventAvro event);
}
