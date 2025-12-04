package ru.yandex.practicum.commerce.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.commerce.interactionapi.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(value = "/api/v1/delivery", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeliveryDto planDelivery(@Valid @RequestBody DeliveryDto request) {
        return deliveryService.planDelivery(request);
    }

    @PostMapping(path = "/successful", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deliverySuccessful(@NotNull @RequestBody UUID orderId) {
        deliveryService.markSuccessful(orderId);
    }

    @PostMapping(path = "/picked", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deliveryPicked(@NotNull @RequestBody UUID orderId) {
        deliveryService.markPicked(orderId);
    }

    @PostMapping(path = "/failed", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deliveryFailed(@NotNull @RequestBody UUID orderId) {
        deliveryService.markFailed(orderId);
    }

    @PostMapping(path = "/cost", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BigDecimal deliveryCost(@Valid @RequestBody OrderDto order) {
        return deliveryService.calculateDeliveryCost(order);
    }
}
