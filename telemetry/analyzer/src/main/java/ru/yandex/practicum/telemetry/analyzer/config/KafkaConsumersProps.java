package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

@Data
@ConfigurationProperties(prefix = "analyzer.streams")
public class KafkaConsumersProps {
    private Stream hub;
    private Stream snapshot;

    @Data
    public static class Stream {
        private Properties consumer;
        private String topic;
        private Duration pollTimeout;
    }
}

