package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.delivery.model.DeliveryEntity;
import ru.yandex.practicum.commerce.delivery.model.DeliveryMapper;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.commerce.interactionapi.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.delivery.DeliveryState;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.exception.delivery.NoDeliveryFoundException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    public DeliveryDto planDelivery(DeliveryDto request) {
        DeliveryEntity entity = new DeliveryEntity();
        // игнорируем deliveryId из запроса – создаём новый
        deliveryMapper.updateEntityFromDto(request, entity);
        entity.setDeliveryState(DeliveryState.CREATED);

        deliveryRepository.save(entity);
        return deliveryMapper.toDto(entity);
    }

    @Override
    public void markPicked(UUID orderId) {
        DeliveryEntity entity = getByOrderId(orderId);
        entity.setDeliveryState(DeliveryState.IN_PROGRESS);
    }

    @Override
    public void markSuccessful(UUID orderId) {
        DeliveryEntity entity = getByOrderId(orderId);
        entity.setDeliveryState(DeliveryState.DELIVERED);
    }

    @Override
    public void markFailed(UUID orderId) {
        DeliveryEntity entity = getByOrderId(orderId);
        entity.setDeliveryState(DeliveryState.FAILED);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateDeliveryCost(OrderDto order) {
        if (order == null || order.getOrderId() == null) {
            throw new NoDeliveryFoundException("Заказ не задан для расчёта доставки");
        }

        double weight = order.getDeliveryWeight() == null ? 0.0 : order.getDeliveryWeight();
        double volume = order.getDeliveryVolume() == null ? 0.0 : order.getDeliveryVolume();
        boolean fragile = Boolean.TRUE.equals(order.getFragile());

        double base = 100.0;
        double cost = base + weight * 10.0 + volume * 5.0 + (fragile ? 150.0 : 0.0);

        return BigDecimal.valueOf(cost);
    }

    private DeliveryEntity getByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException(
                        "Не найдена доставка для заказа " + orderId
                ));
    }
}
