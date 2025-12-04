package ru.yandex.practicum.commerce.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interactionapi.api.WarehouseApi;
import ru.yandex.practicum.commerce.interactionapi.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class WarehouseController implements WarehouseApi {

    private final WarehouseService warehouseService;

    @Override
    public void newProductInWarehouse(@Valid @RequestBody NewProductInWarehouseRequest request) {
        warehouseService.newProductInWarehouse(request);
    }

    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(
            @Valid @RequestBody ShoppingCartDto shoppingCart) {
        return warehouseService.checkProductQuantityEnoughForShoppingCart(shoppingCart);
    }

    @Override
    public void addProductToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest request) {
        warehouseService.addProductToWarehouse(request);
    }

    @PostMapping("/return")
    public void acceptReturn(
            @RequestBody Map<UUID, Integer> products
    ) {
        warehouseService.acceptReturn(products);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}


