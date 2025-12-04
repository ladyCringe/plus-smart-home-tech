package ru.yandex.practicum.commerce.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interactionapi.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.order.service.OrderService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(value = "/api/v1/order", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getClientOrders(@NotEmpty @RequestParam String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
        return orderService.getClientOrders(username);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto createNewOrder(@NotEmpty @RequestParam String username,
                                   @Valid @RequestBody CreateNewOrderRequest request) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
        return orderService.createNewOrder(username, request);
    }

    @PostMapping(path = "/return", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto productReturn(@Valid @RequestBody ProductReturnRequest request) {
        return orderService.productReturn(request);
    }

    @PostMapping(path = "/payment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto payment(@NotNull @RequestBody UUID orderId) {
        return orderService.payment(orderId);
    }

    @PostMapping(path = "/payment/failed", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto paymentFailed(@NotNull @RequestBody UUID orderId) {
        return orderService.paymentFailed(orderId);
    }

    @PostMapping(path = "/delivery", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto delivery(@NotNull @RequestBody UUID orderId) {
        return orderService.delivery(orderId);
    }

    @PostMapping(path = "/delivery/failed", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto deliveryFailed(@NotNull @RequestBody UUID orderId) {
        return orderService.deliveryFailed(orderId);
    }

    @PostMapping(path = "/completed", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto complete(@NotNull @RequestBody UUID orderId) {
        return orderService.complete(orderId);
    }

    @PostMapping(path = "/calculate/total", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto calculateTotal(@NotNull @RequestBody UUID orderId) {
        return orderService.calculateTotalCost(orderId);
    }

    @PostMapping(path = "/calculate/delivery", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto calculateDelivery(@NotNull @RequestBody UUID orderId) {
        return orderService.calculateDeliveryCost(orderId);
    }

    @PostMapping(path = "/assembly", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto assembly(@NotNull @RequestBody UUID orderId) {
        return orderService.assembly(orderId);
    }

    @PostMapping(path = "/assembly/failed", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto assemblyFailed(@NotNull @RequestBody UUID orderId) {
        return orderService.assemblyFailed(orderId);
    }
}
