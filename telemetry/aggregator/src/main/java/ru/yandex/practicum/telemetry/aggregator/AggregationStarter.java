package ru.yandex.practicum.telemetry.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.service.SnapshotService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final SnapshotService snapshotService;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final Producer<String, SensorsSnapshotAvro> producer;

    @Value("${aggregator.topic.sensors}")   private String sensorsTopic;
    @Value("${aggregator.topic.snapshots}") private String snapshotsTopic;

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(sensorsTopic));
            log.info("Aggregator subscribed to {}", sensorsTopic);

            while (true) {
                var records = consumer.poll(Duration.ofSeconds(1));
                if (records.isEmpty()) continue;

                var futures = new ArrayList<Future<RecordMetadata>>();
                for (var rec : records) {
                    var event = rec.value();
                    var maybe = snapshotService.update(event);
                    if (maybe.isPresent()) {
                        var snapshot = maybe.get();
                        var out = new ProducerRecord<>(snapshotsTopic, snapshot.getHubId(), snapshot);
                        futures.add(producer.send(out));
                    }
                }
                for (var f : futures) {
                    f.get();
                }
                producer.flush();
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
            log.info("Aggregator wake up");
        } catch (Exception e) {
            log.error("Aggregator loop error", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                consumer.close();
                producer.close();
            }
        }
    }
}
