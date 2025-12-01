package ru.yandex.practicum.commerce.interactionapi.api;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.dto.store.*;

import java.util.UUID;

@RequestMapping("/api/v1/shopping-store")
public interface ShoppingStoreApi {

    @GetMapping
    ProductsDto getProducts(@RequestParam("category") ProductCategory category,
                            @ModelAttribute Pageable pageable);

    @PutMapping
    ProductDto createNewProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    Boolean setProductQuantityState(@ModelAttribute SetProductQuantityStateRequest request);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable("productId") UUID productId);
}

