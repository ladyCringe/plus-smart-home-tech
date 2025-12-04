package ru.yandex.practicum.commerce.interactionapi.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReturnRequest {

    private UUID orderId;

    @NotNull
    private Map<UUID, Integer> products;
}
