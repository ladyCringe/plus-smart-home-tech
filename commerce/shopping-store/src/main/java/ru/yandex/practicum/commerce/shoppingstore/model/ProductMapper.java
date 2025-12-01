package ru.yandex.practicum.commerce.shoppingstore.model;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interactionapi.dto.store.ProductDto;

import java.util.Objects;

@Component
public class ProductMapper {

    public ProductEntity toEntity(ProductDto productDto) {
        return ProductEntity.builder()
                .productId(Objects.nonNull(productDto.getProductId())
                        ? (productDto.getProductId()) : null)
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .imageSrc(productDto.getImageSrc())
                .quantityState(productDto.getQuantityState())
                .productState(productDto.getProductState())
                .productCategory(productDto.getProductCategory())
                .price(productDto.getPrice())
                .build();
    }

    public ProductDto toDto(ProductEntity product) {
        return ProductDto.builder()
                .productId(Objects.nonNull(product.getProductId())
                        ? (product.getProductId()) : null)
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }
}
