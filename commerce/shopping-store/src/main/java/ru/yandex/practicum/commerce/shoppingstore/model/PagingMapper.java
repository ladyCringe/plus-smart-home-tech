package ru.yandex.practicum.commerce.shoppingstore.model;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interactionapi.dto.store.Pageable;

import java.util.*;

@Component
public class PagingMapper {

    private final Sort.Direction defaultSortDirection = Sort.Direction.ASC;

    public PageRequest toPageRequest(Pageable pageable) {
        int pageNumber = Optional.ofNullable(pageable).map(Pageable::getPage).orElse(0);
        int pageSize = Optional.ofNullable(pageable).map(Pageable::getSize).orElse(Integer.MAX_VALUE);
        return Optional.ofNullable(pageable)
                .map(Pageable::getSort)
                .filter(s -> s.size() == 2 || s.size() == 1)
                .map(s -> Sort.by(toSortDirection(s), s.getFirst()))
                .map(s -> PageRequest.of(pageNumber, pageSize, s))
                .orElse(PageRequest.of(pageNumber, pageSize));
    }

    public List<Map<String, String>> toSortModel(Pageable pageable) {
        HashMap<String, String> sort = new HashMap<>();
        if (pageable.getSort() != null && pageable.getSort().size() == 1) {
            sort.put("direction", defaultSortDirection.name());
            sort.put("property", pageable.getSort().getFirst());
        } else if (pageable.getSort() != null && pageable.getSort().size() == 2) {
            sort.put("direction", pageable.getSort().get(1));
            sort.put("property", pageable.getSort().get(0));
        }
        return Collections.singletonList(sort);
    }

    private Sort.Direction toSortDirection(List<String> sortArray) {
        if (sortArray == null || sortArray.size() < 2) {
            return defaultSortDirection;
        }
        return Sort.Direction.fromString(sortArray.get(1));
    }
}
