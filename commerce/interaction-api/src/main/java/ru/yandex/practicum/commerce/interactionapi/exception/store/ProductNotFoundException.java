package ru.yandex.practicum.commerce.interactionapi.exception.store;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String userMessage;

    public ProductNotFoundException(String userMessage) {
        super(userMessage);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.userMessage = userMessage;
    }
}
