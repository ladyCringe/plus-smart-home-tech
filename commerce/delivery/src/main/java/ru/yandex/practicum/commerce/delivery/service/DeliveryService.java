package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.interactionapi.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    DeliveryDto planDelivery(DeliveryDto request);

    void markPicked(UUID orderId);

    void markSuccessful(UUID orderId);

    void markFailed(UUID orderId);

    BigDecimal calculateDeliveryCost(OrderDto order);
}

