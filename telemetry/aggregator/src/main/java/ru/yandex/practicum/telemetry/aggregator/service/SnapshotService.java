package ru.yandex.practicum.telemetry.aggregator.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SnapshotService {

    private final SnapshotStore store;

    public SnapshotService(SnapshotStore store) {
        this.store = store;
    }

    public Optional<SensorsSnapshotAvro> update(SensorEventAvro event) {
        String hubId = event.getHubId();
        String sensorId = event.getId();

        SensorsSnapshotAvro snapshot = store.get(hubId);
        if (snapshot == null) {
            snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(hubId)
                    .setTimestamp(event.getTimestamp())
                    .setSensorsState(new HashMap<>())
                    .build();
        }

        Map<String, SensorStateAvro> map = snapshot.getSensorsState();
        SensorStateAvro old = map.get(sensorId);

        if (old != null && old.getTimestamp().isAfter(event.getTimestamp())) {
            return Optional.empty();
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        boolean dataChanged = (old == null) || !old.getData().equals(newState.getData());
        boolean timestampAdvanced = snapshot.getTimestamp().isBefore(event.getTimestamp());

        if (!dataChanged) {
            if (timestampAdvanced) {
                snapshot.setTimestamp(event.getTimestamp());
                store.put(hubId, snapshot);
            }
            return Optional.empty();
        }

        map.put(sensorId, newState);
        if (timestampAdvanced) {
            snapshot.setTimestamp(event.getTimestamp());
        }
        store.put(hubId, snapshot);
        return Optional.of(snapshot);
    }

}
