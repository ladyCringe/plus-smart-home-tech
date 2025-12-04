package ru.yandex.practicum.commerce.interactionapi.dto.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

    @NotNull
    private UUID deliveryId;

    @NotNull
    private AddressDto fromAddress;

    @NotNull
    private AddressDto toAddress;

    @NotNull
    private UUID orderId;

    @NotNull
    private DeliveryState deliveryState;
}

