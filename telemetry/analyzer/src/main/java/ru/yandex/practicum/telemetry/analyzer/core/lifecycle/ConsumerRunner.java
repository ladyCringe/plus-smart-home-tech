package ru.yandex.practicum.telemetry.analyzer.core.lifecycle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.context.SmartLifecycle;

import java.time.Duration;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public abstract class ConsumerRunner implements SmartLifecycle {

  private final Consumer<String, SpecificRecordBase> consumer;
  private final String topic;
  private final Duration pollTimeout;
  private volatile boolean running;
  private final Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

  @Override public void start() {
    running = true;
    new Thread(this::loop, threadName()).start();
  }

  @Override public void stop() {
    running = false; consumer.wakeup();
  }

  @Override public boolean isRunning() {
    return running;
  }

  private void loop() {
    try {
      consumer.subscribe(List.of(topic));
      while (running) {
        ConsumerRecords<String, SpecificRecordBase> rs = consumer.poll(pollTimeout);
        int i = 0;
        for (ConsumerRecord<String, SpecificRecordBase> r : rs) {
          handle(r);
          offsets.put(new TopicPartition(r.topic(), r.partition()), new OffsetAndMetadata(r.offset() + 1));
          if ((i++ & 0x0F) == 0) consumer.commitAsync(offsets, (o,e) -> {

          });
        }
        consumer.commitAsync();
      }
    } catch (WakeupException ignore) {
    } catch (Exception e) {
      log.error("consumer loop error", e);
    } finally {
      try {
        consumer.commitSync(offsets);
      } catch (Exception ignore) {

      }
      consumer.close();
    }
  }

  protected abstract void handle(ConsumerRecord<String, SpecificRecordBase> record);

  protected abstract String threadName();
}
