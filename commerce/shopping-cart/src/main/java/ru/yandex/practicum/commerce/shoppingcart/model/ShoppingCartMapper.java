package ru.yandex.practicum.commerce.shoppingcart.model;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interactionapi.dto.cart.ShoppingCartDto;

import java.util.stream.Collectors;

@Component
public class ShoppingCartMapper {

    public ShoppingCartDto toDto(ShoppingCartEntity entity) {
        if (entity == null) {
            return null;
        }

        ShoppingCartDto dto = new ShoppingCartDto();
        dto.setShoppingCartId(entity.getShoppingCartId());
        dto.setProducts(
                entity.getItems().stream()
                        .collect(Collectors.toMap(
                                ShoppingCartItemEntity::getProductId,
                                ShoppingCartItemEntity::getQuantity
                        ))
        );

        return dto;
    }
}
