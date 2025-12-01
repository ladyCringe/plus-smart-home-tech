package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimensionDto {
    @NotBlank
    @Min(1)
    private Double width;

    @NotBlank
    @Min(1)
    private Double height;

    @NotBlank
    @Min(1)
    private Double depth;
}
