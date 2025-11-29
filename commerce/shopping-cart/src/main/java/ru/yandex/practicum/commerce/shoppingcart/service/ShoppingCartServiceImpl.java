package ru.yandex.practicum.commerce.shoppingcart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.exception.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.shoppingcart.client.WarehouseClient;
import ru.yandex.practicum.commerce.shoppingcart.model.ShoppingCartEntity;
import ru.yandex.practicum.commerce.shoppingcart.model.ShoppingCartItemEntity;
import ru.yandex.practicum.commerce.shoppingcart.model.ShoppingCartMapper;
import ru.yandex.practicum.commerce.shoppingcart.repository.ShoppingCartRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCart(String username) {
        ShoppingCartEntity cart = findOrCreateActiveCart(username);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Integer> productsToAdd) {
        ShoppingCartEntity cart = findOrCreateActiveCart(username);

        Map<UUID, ShoppingCartItemEntity> itemsByProductId = cart.getItems().stream()
                .collect(Collectors.toMap(ShoppingCartItemEntity::getProductId, i -> i));

        for (Map.Entry<UUID, Integer> entry : productsToAdd.entrySet()) {
            UUID productId = entry.getKey();
            Integer delta = entry.getValue();

            if (delta == null || delta <= 0) {
                continue;
            }

            ShoppingCartItemEntity item = itemsByProductId.get(productId);
            if (item == null) {
                item = ShoppingCartItemEntity.builder()
                        .shoppingCart(cart)
                        .productId(productId)
                        .quantity(delta)
                        .build();
                cart.getItems().add(item);
                itemsByProductId.put(productId, item);
            } else {
                item.setQuantity(item.getQuantity() + delta);
            }
        }

        ShoppingCartDto cartDtoForCheck = toDto(cart);

        warehouseClient.checkProductQuantityEnoughForShoppingCart(cartDtoForCheck);

        ShoppingCartEntity saved = shoppingCartRepository.save(cart);
        return toDto(saved);
    }

    @Override
    public void deactivateCurrentShoppingCart(String username) {
        shoppingCartRepository.findByUsernameAndActiveIsTrue(username)
                .ifPresent(cart -> {
                    cart.setActive(false);
                    shoppingCartRepository.save(cart);
                });
    }

    @Override
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> productIdsToRemove) {
        ShoppingCartEntity cart = findOrCreateActiveCart(username);

        Set<UUID> toRemove = new HashSet<>(productIdsToRemove);

        boolean anyRemoved = cart.getItems().removeIf(item -> toRemove.contains(item.getProductId()));
        if (!anyRemoved) {
            throw new NoProductsInShoppingCartException("Нет искомых товаров в корзине");
        }

        ShoppingCartEntity saved = shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(saved);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCartEntity cart = findOrCreateActiveCart(username);

        UUID productId = request.getProductId();

        Optional<ShoppingCartItemEntity> itemOpt = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (itemOpt.isEmpty()) {
            throw new NoProductsInShoppingCartException(
                    "Товар " + productId + " не найден в корзине"
            );
        }

        ShoppingCartItemEntity item = itemOpt.get();
        Integer newQuantity = request.getNewQuantity();

        if (newQuantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(newQuantity);
        }

        ShoppingCartEntity saved = shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(saved);
    }

    private ShoppingCartEntity findOrCreateActiveCart(String username) {
        return shoppingCartRepository.findByUsernameAndActiveIsTrue(username)
                .orElseGet(() -> {
                    ShoppingCartEntity cart = ShoppingCartEntity.builder()
                            .shoppingCartId(UUID.randomUUID())
                            .username(username)
                            .active(true)
                            .items(new HashSet<>())
                            .build();
                    return shoppingCartRepository.save(cart);
                });
    }

    private ShoppingCartDto toDto(ShoppingCartEntity cart) {
        ShoppingCartDto dto = new ShoppingCartDto();
        dto.setShoppingCartId(cart.getShoppingCartId());

        Map<UUID, Integer> products = cart.getItems().stream()
                .collect(Collectors.toMap(
                        ShoppingCartItemEntity::getProductId,
                        item -> item.getQuantity()
                ));

        dto.setProducts(products);

        return dto;
    }

}

