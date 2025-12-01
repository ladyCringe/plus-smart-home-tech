package ru.yandex.practicum.commerce.interactionapi.dto.store;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pageable {
    @PositiveOrZero
    private Integer page;

    @Positive
    private Integer size;

    private List<String> sort = new ArrayList<>();
}
