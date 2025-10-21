package ru.yandex.practicum.telemetry.aggregator.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class KafkaClientsConfig {
    private final Properties consumerProps;
    private final Properties producerProps;

    public KafkaClientsConfig(Properties consumerProps, Properties producerProps) {
        this.consumerProps = consumerProps; this.producerProps = producerProps;
    }

    @Bean
    public KafkaConsumer<String, SensorEventAvro> sensorEventConsumer() {
        return new KafkaConsumer<>(consumerProps);
    }

    @Bean
    public Producer<String, SensorsSnapshotAvro> snapshotProducer() {
        return new KafkaProducer<>(producerProps);
    }
}

