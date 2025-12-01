package ru.yandex.practicum.commerce.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shoppingcart.model.ShoppingCartEntity;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, String> {

    Optional<ShoppingCartEntity> findByUsernameAndActiveIsTrue(String username);
}

