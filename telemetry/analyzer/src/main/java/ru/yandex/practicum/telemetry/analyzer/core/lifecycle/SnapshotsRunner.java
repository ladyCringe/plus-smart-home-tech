package ru.yandex.practicum.telemetry.analyzer.core.lifecycle;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConsumersProps;
import ru.yandex.practicum.telemetry.analyzer.core.snapshot.SnapshotDispatcher;

@Component
public class SnapshotsRunner extends ConsumerRunner {
  private final SnapshotDispatcher dispatcher;

  public SnapshotsRunner(@Qualifier("snapshotConsumer") Consumer<String, SpecificRecordBase> c,
                         KafkaConsumersProps props, SnapshotDispatcher dispatcher) {
    super(c, props.getSnapshot().getTopic(), props.getSnapshot().getPollTimeout());
    this.dispatcher = dispatcher;
  }

  @Override protected void handle(ConsumerRecord<String, SpecificRecordBase> r) {
    dispatcher.dispatch(r.value());
  }

  @Override protected String threadName() {
    return "snapshots-runner";
  }
}
