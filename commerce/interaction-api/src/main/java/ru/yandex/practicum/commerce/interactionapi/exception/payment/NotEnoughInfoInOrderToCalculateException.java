package ru.yandex.practicum.commerce.interactionapi.exception.payment;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String userMessage;

    public NotEnoughInfoInOrderToCalculateException(String userMessage) {
        super(userMessage);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.userMessage = userMessage;
    }
}
