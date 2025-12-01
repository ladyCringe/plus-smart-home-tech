package ru.yandex.practicum.commerce.interactionapi.dto.cart;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeProductQuantityRequest {

    @NotBlank
    private UUID productId;

    @NotBlank
    private Integer newQuantity;
}
