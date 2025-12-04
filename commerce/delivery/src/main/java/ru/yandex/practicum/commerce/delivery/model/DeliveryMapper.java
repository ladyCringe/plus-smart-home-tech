package ru.yandex.practicum.commerce.delivery.model;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interactionapi.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;

@Component
public class DeliveryMapper {

    public DeliveryDto toDto(DeliveryEntity entity) {
        if (entity == null) return null;

        return DeliveryDto.builder()
                .deliveryId(entity.getId())
                .orderId(entity.getOrderId())
                .fromAddress(toAddressDto(entity.getFromAddress()))
                .toAddress(toAddressDto(entity.getToAddress()))
                .deliveryState(entity.getDeliveryState())
                .build();
    }

    public void updateEntityFromDto(DeliveryDto dto, DeliveryEntity entity) {
        entity.setOrderId(dto.getOrderId());
        entity.setFromAddress(toEmbeddable(dto.getFromAddress()));
        entity.setToAddress(toEmbeddable(dto.getToAddress()));
        entity.setDeliveryState(dto.getDeliveryState());
    }

    private AddressEmbeddable toEmbeddable(AddressDto dto) {
        if (dto == null) return null;
        AddressEmbeddable e = new AddressEmbeddable();
        e.setCountry(dto.getCountry());
        e.setCity(dto.getCity());
        e.setStreet(dto.getStreet());
        e.setHouse(dto.getHouse());
        e.setFlat(dto.getFlat());
        return e;
    }

    private AddressDto toAddressDto(AddressEmbeddable e) {
        if (e == null) return null;
        AddressDto dto = new AddressDto();
        dto.setCountry(e.getCountry());
        dto.setCity(e.getCity());
        dto.setStreet(e.getStreet());
        dto.setHouse(e.getHouse());
        dto.setFlat(e.getFlat());
        return dto;
    }
}
