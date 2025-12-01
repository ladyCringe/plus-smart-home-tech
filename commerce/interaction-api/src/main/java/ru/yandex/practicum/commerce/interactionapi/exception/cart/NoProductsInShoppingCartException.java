package ru.yandex.practicum.commerce.interactionapi.exception.cart;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoProductsInShoppingCartException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String userMessage;

    public NoProductsInShoppingCartException(String userMessage) {
        super(userMessage);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.userMessage = userMessage;
    }
}
