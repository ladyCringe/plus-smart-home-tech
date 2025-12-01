package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "warehouse_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseProductEntity {

    @Id
    @Column(name = "product_id", columnDefinition = "uuid")
    private UUID productId;

    @Column(name = "fragile", nullable = false)
    private boolean fragile;

    @Column(name = "width", nullable = false)
    private double width;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "depth", nullable = false)
    private double depth;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "quantity", nullable = false)
    private long quantity;
}

