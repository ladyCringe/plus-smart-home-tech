package ru.yandex.practicum.commerce.interactionapi.exception.cart;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotAuthorizedUserException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String userMessage;

    public NotAuthorizedUserException(String userMessage) {
        super(userMessage);
        this.httpStatus = HttpStatus.UNAUTHORIZED;
        this.userMessage = userMessage;
    }
}
