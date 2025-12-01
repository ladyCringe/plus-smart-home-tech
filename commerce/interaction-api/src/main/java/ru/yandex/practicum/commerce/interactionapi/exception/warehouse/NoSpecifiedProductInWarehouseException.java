package ru.yandex.practicum.commerce.interactionapi.exception.warehouse;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSpecifiedProductInWarehouseException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String userMessage;

    public NoSpecifiedProductInWarehouseException(String userMessage) {
        super(userMessage);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.userMessage = userMessage;
    }
}
