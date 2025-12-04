package ru.yandex.practicum.commerce.interactionapi.exception.order;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoOrderFoundException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String userMessage;

    public NoOrderFoundException(String userMessage) {
        super(userMessage);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.userMessage = userMessage;
    }
}
