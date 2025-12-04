package ru.yandex.practicum.commerce.interactionapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;

import java.math.BigDecimal;

@FeignClient(name = "payment")
public interface PaymentClient {

    @PostMapping("/api/v1/payment/productCost")
    BigDecimal productCost(@RequestBody OrderDto order);

    @PostMapping("/api/v1/payment/totalCost")
    BigDecimal totalCost(@RequestBody OrderDto order);

    @PostMapping("/api/v1/payment")
    ru.yandex.practicum.commerce.interactionapi.dto.payment.PaymentDto createPayment(
            @RequestBody OrderDto order
    );
}

