package ru.yandex.practicum.commerce.interactionapi.exception.delivery;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoDeliveryFoundException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String userMessage;

    public NoDeliveryFoundException(String userMessage) {
        super(userMessage);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.userMessage = userMessage;
    }
}
