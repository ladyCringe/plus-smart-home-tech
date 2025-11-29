package ru.yandex.practicum.commerce.interactionapi.dto.store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID productId;

    @NotBlank
    private String productName;

    @NotBlank
    private String description;

    private String imageSrc;

    @NotBlank
    private QuantityState quantityState;

    @NotBlank
    private ProductState productState;

    @NotBlank
    private ProductCategory productCategory;

    @Positive
    @NotBlank
    private Double price;

}
