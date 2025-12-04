package ru.yandex.practicum.commerce.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/productCost")
    public BigDecimal productCost(@Valid @RequestBody OrderDto order) {
        return paymentService.calculateProductCost(order);
    }

    @PostMapping("/totalCost")
    public BigDecimal totalCost(@Valid @RequestBody OrderDto order) {
        return paymentService.calculateTotalCost(order);
    }

    @PostMapping
    public PaymentDto payment(@Valid @RequestBody OrderDto order) {
        return paymentService.createPayment(order);
    }

    @PostMapping("/refund")
    public void paymentSuccess(@NotNull @RequestBody UUID paymentId) {
        paymentService.paymentSuccess(paymentId);
    }

    @PostMapping("/failed")
    public void paymentFailed(@NotNull @RequestBody UUID paymentId) {
        paymentService.paymentFailed(paymentId);
    }
}
