package ru.yandex.practicum.commerce.interactionapi.api;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/v1/shopping-cart")
public interface ShoppingCartApi {

    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam("username") String username);

    @PutMapping
    ShoppingCartDto addProductToShoppingCart(@RequestParam("username") String username,
                                             @RequestBody Map<UUID, Integer> products);

    @DeleteMapping
    void deactivateCurrentShoppingCart(@RequestParam("username") String username);

    @PostMapping("/remove")
    ShoppingCartDto removeFromShoppingCart(@RequestParam("username") String username,
                                           @RequestBody List<UUID> productIds);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantity(@RequestParam("username") String username,
                                          @RequestBody ChangeProductQuantityRequest request);
}

