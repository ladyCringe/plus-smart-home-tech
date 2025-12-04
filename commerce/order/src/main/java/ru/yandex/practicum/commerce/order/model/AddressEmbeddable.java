package ru.yandex.practicum.commerce.order.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class AddressEmbeddable {
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}
