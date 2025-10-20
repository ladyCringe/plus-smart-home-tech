package ru.yandex.practicum.telemetry.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.HubEventService;
import ru.yandex.practicum.telemetry.collector.service.SensorEventService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final SensorEventService sensorEventService;
    private final HubEventService hubEventService;
    private final SensorEventMapper sensorMapper;
    private final HubEventMapper hubMapper;

    @Override
    public void collectSensorEvent(SensorEventProto event, StreamObserver<Empty> responseObserver) {
        try {
            log.info("Request to create a sensor event: {}", event);
            SensorEvent sensorEvent = sensorMapper.map(event);
            log.info("gRPC: sensor event {}", sensorEvent);
            sensorEventService.collectSensorEvent(sensorEvent);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL.withDescription(e.getMessage()).withCause(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto event, StreamObserver<Empty> responseObserver) {
        try {
            log.info("Request to create a hub event: {}", event);
            HubEvent hubEvent = hubMapper.map(event);
            log.info("gRPC: hub event {}", hubEvent);
            hubEventService.collectHubEvent(hubEvent);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL.withDescription(e.getMessage()).withCause(e)
            ));
        }
    }
}
