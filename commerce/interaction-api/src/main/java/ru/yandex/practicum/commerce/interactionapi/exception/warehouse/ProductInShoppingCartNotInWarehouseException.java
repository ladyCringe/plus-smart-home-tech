package ru.yandex.practicum.commerce.interactionapi.exception.warehouse;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductInShoppingCartNotInWarehouseException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String userMessage;

    public ProductInShoppingCartNotInWarehouseException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
    }
}
