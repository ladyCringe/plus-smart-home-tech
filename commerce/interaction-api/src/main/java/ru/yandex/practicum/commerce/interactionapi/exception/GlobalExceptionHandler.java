package ru.yandex.practicum.commerce.interactionapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interactionapi.exception.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.interactionapi.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.interactionapi.exception.store.ProductNotFoundException;
import ru.yandex.practicum.commerce.interactionapi.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interactionapi.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.commerce.interactionapi.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProductNotFoundException> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    public ResponseEntity<NotAuthorizedUserException> handleNotAuthorized(NotAuthorizedUserException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex);
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    public ResponseEntity<NoProductsInShoppingCartException> handleNoProducts(NoProductsInShoppingCartException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex);
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ResponseEntity<SpecifiedProductAlreadyInWarehouseException> handleSpecifiedProductAlready(
            SpecifiedProductAlreadyInWarehouseException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouseException.class)
    public ResponseEntity<ProductInShoppingCartLowQuantityInWarehouseException> handleLowQuantity(
            ProductInShoppingCartLowQuantityInWarehouseException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<NoSpecifiedProductInWarehouseException> handleNoSpecifiedProduct(
            NoSpecifiedProductInWarehouseException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex);
    }
}
