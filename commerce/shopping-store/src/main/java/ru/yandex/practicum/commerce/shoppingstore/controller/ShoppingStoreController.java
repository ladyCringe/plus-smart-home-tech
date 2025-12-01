package ru.yandex.practicum.commerce.shoppingstore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.api.ShoppingStoreApi;
import ru.yandex.practicum.commerce.interactionapi.dto.store.*;
import ru.yandex.practicum.commerce.shoppingstore.service.ShoppingStoreService;

import java.util.*;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreApi {

    private final ShoppingStoreService shoppingStoreService;

    @Override
    public ProductsDto getProducts(ProductCategory category, Pageable pageable) {
        Page<ProductDto> content = shoppingStoreService
                .getProducts(category, pageable);
        HashMap<String, String> sort = new HashMap<>();
        if (pageable.getSort() != null && !pageable.getSort().isEmpty()) {
            sort.put("direction", pageable.getSort().get(1));
            sort.put("property", pageable.getSort().get(0));
        }
        return ProductsDto.builder()
                .content(content.getContent())
                .sort(Collections.singletonList(sort))
                .build();
    }

    @Override
    public ProductDto createNewProduct(ProductDto productDto) {
        return shoppingStoreService.createNewProduct(productDto);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    @Override
    public Boolean removeProductFromStore(UUID productId) {
        return shoppingStoreService.removeProductFromStore(productId);
    }

    @Override
    public Boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        return shoppingStoreService.setProductQuantityState(request);
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        return shoppingStoreService.getProduct(productId);
    }
}


