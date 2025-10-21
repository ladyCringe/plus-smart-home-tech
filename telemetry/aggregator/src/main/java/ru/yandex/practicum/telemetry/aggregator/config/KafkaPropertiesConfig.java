package ru.yandex.practicum.telemetry.aggregator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaPropertiesConfig {
    @ConfigurationProperties(prefix = "aggregator.kafka.consumer")
    @Bean
    public Properties consumerProps() {
        return new Properties();
    }

    @ConfigurationProperties(prefix = "aggregator.kafka.producer")
    @Bean
    public Properties producerProps() {
        return new Properties();
    }
}
