package ru.yandex.practicum.commerce.shoppingcart.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "shopping_cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCartEntity shoppingCart;

    @Column(name = "product_id", nullable = false, length = 36)
    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
