package ru.yandex.practicum.telemetry.analyzer.core.lifecycle;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConsumersProps;
import ru.yandex.practicum.telemetry.analyzer.core.hub.HubEventDispatcher;

@Component
public class HubEventsRunner extends ConsumerRunner {
  private final HubEventDispatcher dispatcher;

  public HubEventsRunner(@Qualifier("hubConsumer") Consumer<String, SpecificRecordBase> c,
                         KafkaConsumersProps props, HubEventDispatcher dispatcher) {
    super(c, props.getHub().getTopic(), props.getHub().getPollTimeout());
    this.dispatcher = dispatcher;
  }

  @Override
  protected void handle(ConsumerRecord<String, SpecificRecordBase> r) {
    dispatcher.dispatch(r.value());
  }

  @Override
  protected String threadName() {
    return "hub-events-runner";
  }
}
