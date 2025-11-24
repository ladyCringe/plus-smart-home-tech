package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
public abstract class TopicProcessor implements Runnable {

    @Value(value = "${analyzer.kafka.offset-fix-count}")
    private int offSetFixCount;

    private final String topicConsumer;
    private final Consumer<String, SpecificRecordBase> consumer;
    private final Duration pollTimeout;

    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public TopicProcessor(Properties properties, String topicConsumer, Duration pollTimeout) {
        this.consumer = new KafkaConsumer<String, SpecificRecordBase>(properties);
        this.topicConsumer = topicConsumer;
        this.pollTimeout = pollTimeout;
    }

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            log.info("Receiving messages of topicConsumer: {}", topicConsumer);
            consumer.subscribe(List.of(topicConsumer));

            while (true) {

                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(pollTimeout);
                int count = 0;
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    handleRecord(record);
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
            log.warn("The poll will be interrupted");
        } catch (Exception e) {
            log.error("Handle events exception", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Closing consumer");
                consumer.close();
            }
        }
    }

    private void manageOffsets(ConsumerRecord<String, SpecificRecordBase> record, int count,
                               Consumer<String, SpecificRecordBase> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
        if (count % offSetFixCount == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (Objects.nonNull(exception)) {
                    log.warn("Offset fixation exception: {}", offsets, exception);
                }
            });
        }
    }

    public abstract void handleRecord(ConsumerRecord<String, SpecificRecordBase> record);

}
