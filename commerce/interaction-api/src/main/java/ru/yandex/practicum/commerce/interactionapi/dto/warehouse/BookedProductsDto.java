package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookedProductsDto {

    @NotBlank
    private Double deliveryWeight;

    @NotBlank
    private Double deliveryVolume;

    @NotBlank
    private Boolean fragile;

}
