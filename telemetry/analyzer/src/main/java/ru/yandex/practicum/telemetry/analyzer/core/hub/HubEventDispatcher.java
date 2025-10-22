package ru.yandex.practicum.telemetry.analyzer.core.hub;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HubEventDispatcher {

  private final Map<Class<?>, HubEventHandler<?>> handlers;

  public HubEventDispatcher(List<HubEventHandler<?>> handlers) {
    this.handlers = handlers.stream().collect(Collectors.toMap(HubEventHandler::payloadType, Function.identity()));
  }

  public void dispatch(SpecificRecordBase rec) {
    if (!(rec instanceof HubEventAvro ev)) return;
    var h = handlers.get(ev.getPayload().getClass());
    if (h != null) h.handle(ev);
  }
}
