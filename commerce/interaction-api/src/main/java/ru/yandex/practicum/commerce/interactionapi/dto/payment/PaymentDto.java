package ru.yandex.practicum.commerce.interactionapi.dto.payment;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDto {

    private UUID paymentId;
    private BigDecimal totalPayment;
    private BigDecimal deliveryTotal;
    private BigDecimal feeTotal;
    private PaymentStatus paymentStatus;
}
