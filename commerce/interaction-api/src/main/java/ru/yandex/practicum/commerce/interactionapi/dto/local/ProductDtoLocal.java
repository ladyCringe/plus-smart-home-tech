package ru.yandex.practicum.commerce.interactionapi.dto.local;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDtoLocal {

    private BigDecimal price;

    private Double deliveryWeight;

    private Double deliveryVolume;

    private Boolean fragile;
}
