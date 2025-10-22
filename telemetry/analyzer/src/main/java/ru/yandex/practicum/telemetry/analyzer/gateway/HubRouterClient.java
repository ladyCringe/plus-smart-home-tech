package ru.yandex.practicum.telemetry.analyzer.gateway;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequestProto;
import ru.yandex.practicum.telemetry.analyzer.config.GrpcClientConfig;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubRouterClient {
  private final GrpcClientConfig grpc;

  public void send(DeviceActionRequestProto req) {
    log.trace("gRPC → {}", req.getAllFields());
    Empty empty = grpc.stub().handleDeviceAction(req);
    log.trace("gRPC ← {}", empty);
  }
}
