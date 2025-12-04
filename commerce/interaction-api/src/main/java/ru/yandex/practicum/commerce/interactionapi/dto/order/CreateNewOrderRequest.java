package ru.yandex.practicum.commerce.interactionapi.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.yandex.practicum.commerce.interactionapi.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewOrderRequest {
    @NotBlank
    private ShoppingCartDto shoppingCart;

    @NotBlank
    private AddressDto deliveryAddress;
}
