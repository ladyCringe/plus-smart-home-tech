package ru.yandex.practicum.commerce.interactionapi.dto.cart;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {

    @NotBlank
    private UUID shoppingCartId;

    @NotBlank
    private Map<UUID, Integer> products;
}
