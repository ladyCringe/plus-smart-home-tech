package ru.yandex.practicum.commerce.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.client.OrderClient;
import ru.yandex.practicum.commerce.interactionapi.client.ShoppingStoreClient;
import ru.yandex.practicum.commerce.interactionapi.dto.local.ProductDtoLocal;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.payment.PaymentStatus;
import ru.yandex.practicum.commerce.interactionapi.exception.order.NoOrderFoundException;
import ru.yandex.practicum.commerce.interactionapi.exception.payment.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.commerce.payment.model.PaymentEntity;
import ru.yandex.practicum.commerce.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateProductCost(OrderDto order) {
        validateOrderForCalculation(order);

        BigDecimal sum = BigDecimal.ZERO;

        for (Map.Entry<UUID, Integer> entry : order.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();

            if (quantity == null || quantity <= 0) {
                continue;
            }

            ProductDtoLocal product = shoppingStoreClient.getProduct(productId);
            if (product == null || product.getPrice() == null) {
                throw new NotEnoughInfoInOrderToCalculateException(
                        "Не удалось получить цену для товара " + productId
                );
            }

            BigDecimal price = product.getPrice();
            sum = sum.add(price.multiply(BigDecimal.valueOf(quantity)));
        }

        return sum;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalCost(OrderDto order) {
        validateOrderForCalculation(order);

        BigDecimal productCost = calculateProductCost(order);
        BigDecimal deliveryPrice = order.getDeliveryPrice() == null
                ? BigDecimal.ZERO
                : order.getDeliveryPrice();

        BigDecimal subtotal = productCost.add(deliveryPrice);
        BigDecimal fee = subtotal.multiply(BigDecimal.valueOf(0.1));
        BigDecimal total = subtotal.add(fee);

        return total;
    }

    @Override
    public PaymentDto createPayment(OrderDto order) {
        validateOrderForCalculation(order);

        BigDecimal productCost = calculateProductCost(order);
        BigDecimal deliveryPrice = order.getDeliveryPrice() == null
                ? BigDecimal.ZERO
                : order.getDeliveryPrice();

        BigDecimal subtotal = productCost.add(deliveryPrice);
        BigDecimal fee = subtotal.multiply(BigDecimal.valueOf(0.1));
        BigDecimal total = subtotal.add(fee);

        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(order.getOrderId());
        entity.setProductCost(productCost);
        entity.setDeliveryTotal(deliveryPrice);
        entity.setFeeTotal(fee);
        entity.setTotalPayment(total);
        entity.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(entity);

        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(entity.getId());
        dto.setTotalPayment(entity.getTotalPayment());
        dto.setDeliveryTotal(entity.getDeliveryTotal());
        dto.setFeeTotal(entity.getFeeTotal());
        dto.setPaymentStatus(entity.getStatus());

        return dto;
    }

    @Override
    public void paymentSuccess(UUID paymentId) {
        PaymentEntity entity = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException(
                        "Не найдена оплата с id=" + paymentId
                ));

        entity.setStatus(PaymentStatus.SUCCESS);
        orderClient.paymentSuccess(entity.getOrderId());
    }

    @Override
    public void paymentFailed(UUID paymentId) {
        PaymentEntity entity = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException(
                        "Не найдена оплата с id=" + paymentId
                ));

        entity.setStatus(PaymentStatus.FAILED);
        orderClient.paymentFailed(entity.getOrderId());
    }

    private void validateOrderForCalculation(OrderDto order) {
        if (order == null
                || order.getOrderId() == null
                || order.getProducts() == null
                || order.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "Недостаточно информации в заказе для расчёта"
            );
        }
    }
}
