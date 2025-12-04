package ru.yandex.practicum.commerce.payment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.commerce.interactionapi.dto.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class PaymentEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(precision = 19, scale = 2)
    private BigDecimal productCost;

    @Column(precision = 19, scale = 2)
    private BigDecimal deliveryTotal;

    @Column(precision = 19, scale = 2)
    private BigDecimal feeTotal;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalPayment;

    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}

