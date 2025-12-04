package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippedToDeliveryRequest {

    @NotBlank
    private UUID orderId;
    @NotBlank
    private UUID deliveryId;
}
