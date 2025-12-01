package ru.yandex.practicum.commerce.shoppingcart.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "shopping_carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartEntity {

    @Id
    @Column(name = "shopping_cart_id", length = 36)
    private UUID shoppingCartId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ShoppingCartItemEntity> items = new HashSet<>();
}
