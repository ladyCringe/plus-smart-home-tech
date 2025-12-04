package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    BigDecimal calculateProductCost(OrderDto order);

    BigDecimal calculateTotalCost(OrderDto order);

    PaymentDto createPayment(OrderDto order);

    void paymentSuccess(UUID paymentId);

    void paymentFailed(UUID paymentId);
}
