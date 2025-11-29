package ru.yandex.practicum.commerce.shoppingstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.store.*;
import ru.yandex.practicum.commerce.interactionapi.exception.store.ProductNotFoundException;
import ru.yandex.practicum.commerce.shoppingstore.model.ProductMapper;
import ru.yandex.practicum.commerce.shoppingstore.model.ProductEntity;
import ru.yandex.practicum.commerce.shoppingstore.repository.ProductRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        PageRequest pageRequest =  Optional.ofNullable(pageable)
                .filter(it -> it.getPage() != null && it.getSize() != null)
                .map(it -> PageRequest.of(it.getPage(), it.getSize(),
                        Sort.by(Sort.Direction.fromString(it.getSort().get(1)), it.getSort().getFirst())))
                .orElse(PageRequest.of(0, Integer.MAX_VALUE));
        return productRepository.findAllByProductCategory(category, pageRequest)
                .map(productMapper::toDto);
    }

    @Override
    public ProductDto createNewProduct(ProductDto productDto) {
        ProductEntity entity = productMapper.toEntity(productDto);
        entity.setProductId(null);
        ProductEntity saved = productRepository.save(entity);
        return productMapper.toDto(saved);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        UUID id = productDto.getProductId();
        if (id == null) {
            throw new ProductNotFoundException("Product id must be provided for update");
        }

        ProductEntity existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));

        existing.setProductName(productDto.getProductName());
        existing.setDescription(productDto.getDescription());
        existing.setImageSrc(productDto.getImageSrc());
        existing.setQuantityState(productDto.getQuantityState());
        existing.setProductState(productDto.getProductState());
        existing.setProductCategory(productDto.getProductCategory());
        existing.setPrice(productDto.getPrice());

        ProductEntity saved = productRepository.save(existing);
        return productMapper.toDto(saved);
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        ProductEntity existing = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));

        existing.setProductState(ProductState.DEACTIVATE);
        productRepository.save(existing);
        return true;
    }

    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        UUID id = request.getProductId();
        ProductEntity existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));

        existing.setQuantityState(request.getQuantityState());
        productRepository.save(existing);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(UUID productId) {
        ProductEntity existing = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));
        return productMapper.toDto(existing);
    }
}

