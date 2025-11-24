package ru.yandex.practicum.telemetry.analyzer.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conditions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ConditionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private ConditionOperation operation;

    @Column(name = "value")
    private Integer value;

    @Transient
    private String sensorId;
}
