package ru.yandex.practicum.commerce.interactionapi.dto.store;


import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsDto {

    private List<ProductDto> content;

    private List<Map<String, String>> sort;
}
