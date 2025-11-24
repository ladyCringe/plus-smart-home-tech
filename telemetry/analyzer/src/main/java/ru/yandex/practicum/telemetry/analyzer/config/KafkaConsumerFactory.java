package ru.yandex.practicum.telemetry.analyzer.config;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerFactory {

    @Bean("hubConsumer")
    public Consumer<String, SpecificRecordBase> hubConsumer(KafkaConsumersProps props) {
        return new KafkaConsumer<>(props.getHub().getConsumer());
    }

    @Bean("snapshotConsumer")
    public Consumer<String, SpecificRecordBase> snapshotConsumer(KafkaConsumersProps props) {
        return new KafkaConsumer<>(props.getSnapshot().getConsumer());
    }
}

