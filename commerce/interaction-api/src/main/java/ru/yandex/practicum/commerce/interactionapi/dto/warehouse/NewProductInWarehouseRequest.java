package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductInWarehouseRequest {

    @NotBlank
    private UUID productId;

    private Boolean fragile;

    @NotBlank
    private DimensionDto dimension;

    @NotBlank
    @Min(1)
    private Double weight;
}
