package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.*;
import ru.yandex.practicum.commerce.interactionapi.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interactionapi.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.commerce.interactionapi.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.model.OrderBookingEntity;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProductEntity;
import ru.yandex.practicum.commerce.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseProductRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseProductRepository warehouseProductRepository;
    private final OrderBookingRepository orderBookingRepository;

    @Value("${warehouse.address.country:Kazakhstan}")
    private String country;

    @Value("${warehouse.address.city:Astana}")
    private String city;

    @Value("${warehouse.address.street:Main street}")
    private String street;

    @Value("${warehouse.address.house:1}")
    private String house;

    @Value("${warehouse.address.flat:1}")
    private String flat;

    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequest request) {
        UUID productId = request.getProductId();

        if (warehouseProductRepository.existsById(productId)) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    "Товар с id " + productId + " уже зарегистрирован на складе"
            );
        }

        DimensionDto d = request.getDimension();

        WarehouseProductEntity entity = WarehouseProductEntity.builder()
                .productId(productId)
                .fragile(Boolean.TRUE.equals(request.getFragile()))
                .width(d.getWidth())
                .height(d.getHeight())
                .depth(d.getDepth())
                .weight(request.getWeight())
                .quantity(0L)
                .build();

        warehouseProductRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart) {
        Map<UUID, Integer> products = shoppingCart.getProducts();

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean anyFragile = false;

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            UUID productId = entry.getKey();
            long requestedQty = entry.getValue();

            WarehouseProductEntity entity = warehouseProductRepository.findById(productId)
                    .orElseThrow(() -> new ProductInShoppingCartLowQuantityInWarehouseException(
                            "Товар " + productId + " отсутствует на складе"
                    ));

            if (entity.getQuantity() < requestedQty) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException(
                        "Товар " + productId + " доступен в количестве " + entity.getQuantity()
                                + ", запрошено " + requestedQty
                );
            }

            double volumeOne = entity.getWidth() * entity.getHeight() * entity.getDepth();
            totalVolume += volumeOne * requestedQty;
            totalWeight += entity.getWeight() * requestedQty;
            if (entity.isFragile()) {
                anyFragile = true;
            }
        }

        BookedProductsDto dto = new BookedProductsDto();
        dto.setDeliveryWeight(totalWeight);
        dto.setDeliveryVolume(totalVolume);
        dto.setFragile(anyFragile);
        return dto;
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        UUID productId = request.getProductId();

        WarehouseProductEntity entity = warehouseProductRepository.findById(productId)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        "Нет информации о товаре " + productId + " на складе"
                ));

        long newQuantity = entity.getQuantity() + request.getQuantity();
        entity.setQuantity(newQuantity);
        warehouseProductRepository.save(entity);
    }

    @Override
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request) {
        Map<UUID, Integer> products = request.getProducts();
        if (products == null || products.isEmpty()) {
            throw new NoSpecifiedProductInWarehouseException("Нет товаров для сборки заказа");
        }

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean fragile = false;

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            UUID productId = entry.getKey();
            int qty = entry.getValue() == null ? 0 : entry.getValue();

            WarehouseProductEntity product = warehouseProductRepository.findById(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                            "Нет информации о товаре " + productId + " на складе"
                    ));

            if (product.getQuantity() < qty) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException(
                        "Недостаточное количество товара " + productId + " на складе"
                );
            }

            product.setQuantity(product.getQuantity() - qty);

            totalWeight += product.getWeight() * qty;
            totalVolume += product.getWidth() * product.getHeight() * product.getDepth() * qty;
            if (product.isFragile()) {
                fragile = true;
            }
        }

        // создаём/обновляем бронирование заказа
        OrderBookingEntity booking = orderBookingRepository.findByOrderId(request.getOrderId())
                .orElseGet(() -> {
                    OrderBookingEntity e = new OrderBookingEntity();
                    e.setOrderId(request.getOrderId());
                    return e;
                });

        booking.setProducts(new HashMap<>(products));
        orderBookingRepository.save(booking);

        return new BookedProductsDto(totalWeight, totalVolume, fragile);
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        OrderBookingEntity booking = orderBookingRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        "Не найдено бронирование для заказа " + request.getOrderId()
                ));

        booking.setDeliveryId(request.getDeliveryId());
    }

    @Override
    public void acceptReturn(Map<UUID, Integer> products) {
        if (products == null || products.isEmpty()) {
            return;
        }

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            UUID productId = entry.getKey();
            int qty = entry.getValue() == null ? 0 : entry.getValue();

            WarehouseProductEntity product = warehouseProductRepository.findById(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                            "Нет информации о товаре " + productId + " на складе"
                    ));

            product.setQuantity(product.getQuantity() + qty);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDto getWarehouseAddress() {
        AddressDto dto = new AddressDto();
        dto.setCountry(country);
        dto.setCity(city);
        dto.setStreet(street);
        dto.setHouse(house);
        dto.setFlat(flat);
        return dto;
    }
}

