package ru.yandex.practicum.commerce.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.client.DeliveryClient;
import ru.yandex.practicum.commerce.interactionapi.client.PaymentClient;
import ru.yandex.practicum.commerce.interactionapi.client.ShoppingStoreClient;
import ru.yandex.practicum.commerce.interactionapi.client.WarehouseClient;
import ru.yandex.practicum.commerce.interactionapi.dto.local.ProductDtoLocal;
import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderState;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interactionapi.exception.order.NoOrderFoundException;
import ru.yandex.practicum.commerce.interactionapi.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.order.model.OrderEntity;
import ru.yandex.practicum.commerce.order.model.OrderMapper;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final WarehouseClient warehouseClient;
    private final ShoppingStoreClient shoppingStoreClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getClientOrders(String username) {
        return orderRepository.findAllByUsername(username).stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto createNewOrder(String username, CreateNewOrderRequest request) {
        if (request.getShoppingCart() == null
                || request.getShoppingCart().getProducts() == null
                || request.getShoppingCart().getProducts().isEmpty()) {
            throw new NoSpecifiedProductInWarehouseException("Нет товаров для заказа");
        }

        OrderEntity entity = new OrderEntity();
        entity.setUsername(username);
        entity.setShoppingCartId(request.getShoppingCart().getShoppingCartId());
        entity.setProducts(request.getShoppingCart().getProducts());
        entity.setState(OrderState.NEW);
        entity.setDeliveryAddress(orderMapper.toAddressEmbeddable(request.getDeliveryAddress()));

        calcPhysicalParams(entity);

        orderRepository.save(entity);
        return orderMapper.toOrderDto(entity);
    }

    private void calcPhysicalParams(OrderEntity entity) {
        double weight = 0.0;
        double volume = 0.0;
        boolean fragile = false;

        for (Map.Entry<UUID, Integer> e : entity.getProducts().entrySet()) {
            ProductDtoLocal product = shoppingStoreClient.getProduct(e.getKey());
            if (product == null) continue;

            int qty = e.getValue();
            if (product.getDeliveryWeight() != null) {
                weight += product.getDeliveryWeight() * qty;
            }
            if (product.getDeliveryVolume() != null) {
                volume += product.getDeliveryVolume() * qty;
            }
            if (Boolean.TRUE.equals(product.getFragile())) {
                fragile = true;
            }
        }

        entity.setDeliveryWeight(weight);
        entity.setDeliveryVolume(volume);
        entity.setFragile(fragile);
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        OrderEntity entity = getOrder(orderId);
        warehouseClient.assembly(orderId, entity.getProducts());
        entity.setState(OrderState.ASSEMBLED);
        return orderMapper.toOrderDto(entity);
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        OrderEntity entity = getOrder(orderId);
        warehouseClient.assemblyFailed(orderId);
        entity.setState(OrderState.ASSEMBLY_FAILED);
        return orderMapper.toOrderDto(entity);
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        OrderEntity entity = getOrder(orderId);

        OrderDto orderDto = orderMapper.toOrderDto(entity);
        BigDecimal cost = deliveryClient.deliveryCost(orderDto);

        entity.setDeliveryPrice(cost);
        entity.setState(OrderState.ON_DELIVERY);
        orderRepository.save(entity);

        return orderMapper.toOrderDto(entity);
    }


    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        OrderEntity entity = getOrder(orderId);

        OrderDto currentOrder = orderMapper.toOrderDto(entity);

        BigDecimal productCost = paymentClient.productCost(currentOrder);
        entity.setProductPrice(productCost);

        OrderDto orderForTotal = orderMapper.toOrderDto(entity);
        BigDecimal totalCost = paymentClient.totalCost(orderForTotal);
        entity.setTotalPrice(totalCost);

        entity.setState(OrderState.ON_PAYMENT);

        return orderMapper.toOrderDto(entity);
    }

    @Override
    public OrderDto payment(UUID orderId) {
        OrderEntity entity = getOrder(orderId);
        entity.setState(OrderState.PAID);
        return orderMapper.toOrderDto(entity);
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        OrderEntity entity = getOrder(orderId);
        entity.setState(OrderState.PAYMENT_FAILED);
        return orderMapper.toOrderDto(entity);
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        OrderEntity entity = getOrder(orderId);
        entity.setState(OrderState.DELIVERED);
        return orderMapper.toOrderDto(entity);
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        OrderEntity entity = getOrder(orderId);
        entity.setState(OrderState.DELIVERY_FAILED);
        warehouseClient.returnProducts(orderId, entity.getProducts());
        return orderMapper.toOrderDto(entity);
    }

    @Override
    public OrderDto complete(UUID orderId) {
        OrderEntity entity = getOrder(orderId);
        entity.setState(OrderState.COMPLETED);
        return orderMapper.toOrderDto(entity);
    }

    @Override
    public OrderDto productReturn(ProductReturnRequest request) {
        OrderEntity entity = getOrder(request.getOrderId());
        warehouseClient.returnProducts(request.getOrderId(), request.getProducts());
        entity.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.toOrderDto(entity);
    }

    private OrderEntity getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order " + orderId + " not found"));
    }
}
