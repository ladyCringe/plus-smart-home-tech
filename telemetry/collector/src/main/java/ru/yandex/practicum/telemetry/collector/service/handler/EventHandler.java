package ru.yandex.practicum.telemetry.collector.service.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.telemetry.collector.config.KafkaEventProducer;

@Slf4j
public abstract class EventHandler<T, A extends SpecificRecordBase, B> {

    public abstract B getMessageType();

    public abstract A handle(T event);

    public void handleEvent(KafkaEventProducer kafkaProducer, String topic, String key, T event) {
        log.info("New event {}", event);
        A avroEvent = handle(event);
        log.info("Sending event to Kafka: {}", avroEvent);

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, key, avroEvent);
        kafkaProducer.getProducer().send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Error sending record", exception);
            } else {
                log.info("Was sent to Kafka topic={} offset={}", metadata.topic(), metadata.offset());
            }
        });
    }
}
