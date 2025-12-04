package ru.yandex.practicum.commerce.order.model;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;

@Component
public class OrderMapper {

    public OrderDto toOrderDto(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        return OrderDto.builder()
                .orderId(entity.getId())
                .shoppingCartId(entity.getShoppingCartId())
                .products(entity.getProducts())
                .paymentId(entity.getPaymentId())
                .deliveryId(entity.getDeliveryId())
                .state(entity.getState())
                .deliveryWeight(entity.getDeliveryWeight())
                .deliveryVolume(entity.getDeliveryVolume())
                .fragile(entity.getFragile())
                .totalPrice(entity.getTotalPrice())
                .deliveryPrice(entity.getDeliveryPrice())
                .productPrice(entity.getProductPrice())
                .build();
    }

    public void updateEntityFromOrderDto(OrderDto dto, OrderEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setShoppingCartId(dto.getShoppingCartId());
        entity.setProducts(dto.getProducts());
        entity.setPaymentId(dto.getPaymentId());
        entity.setDeliveryId(dto.getDeliveryId());
        entity.setState(dto.getState());

        entity.setDeliveryWeight(dto.getDeliveryWeight());
        entity.setDeliveryVolume(dto.getDeliveryVolume());
        entity.setFragile(dto.getFragile());

        entity.setTotalPrice(dto.getTotalPrice());
        entity.setDeliveryPrice(dto.getDeliveryPrice());
        entity.setProductPrice(dto.getProductPrice());
    }

    public AddressEmbeddable toAddressEmbeddable(AddressDto dto) {
        if (dto == null) {
            return null;
        }

        AddressEmbeddable e = new AddressEmbeddable();
        e.setCountry(dto.getCountry());
        e.setCity(dto.getCity());
        e.setStreet(dto.getStreet());
        e.setHouse(dto.getHouse());
        e.setFlat(dto.getFlat());
        return e;
    }

    public AddressDto toAddressDto(AddressEmbeddable e) {
        if (e == null) {
            return null;
        }

        AddressDto dto = new AddressDto();
        dto.setCountry(e.getCountry());
        dto.setCity(e.getCity());
        dto.setStreet(e.getStreet());
        dto.setHouse(e.getHouse());
        dto.setFlat(e.getFlat());
        return dto;
    }

    private double nz(Double v) {
        return v == null ? 0.0 : v;
    }
}
