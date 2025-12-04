package ru.yandex.practicum.commerce.interactionapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.commerce.interactionapi.dto.local.ProductDtoLocal;

import java.util.UUID;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient {

    @GetMapping("/internal/products/{id}")
    ProductDtoLocal getProduct(@PathVariable UUID id);
}

