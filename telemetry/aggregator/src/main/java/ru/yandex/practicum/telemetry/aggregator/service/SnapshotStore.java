package ru.yandex.practicum.telemetry.aggregator.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SnapshotStore  {

    private final Map<String, SensorsSnapshotAvro> byHub = new ConcurrentHashMap<>();

    public SensorsSnapshotAvro get(String hubId) {
        return byHub.get(hubId);
    }

    public void put(String hubId, SensorsSnapshotAvro snapshot) {
        byHub.put(hubId, snapshot);
    }
}
