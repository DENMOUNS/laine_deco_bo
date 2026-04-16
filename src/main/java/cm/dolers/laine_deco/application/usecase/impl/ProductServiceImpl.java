package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ProductMapper;
import cm.dolers.laine_deco.application.usecase.ProductService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ProductException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductJpaRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDetailResponse createProduct(CreateProductRequest request) {
        log.info("Creating product: {}", request.sku());

        if (productRepository.findBySku(request.sku()).isPresent()) {
            throw new ProductException(ErrorCode.PRODUCT_SKU_EXISTS, "SKU: " + request.sku());
        }

        try {
            var product = productMapper.createFromRequest(request);
            var saved = productRepository.save(product);
            log.info("Product created: {}", saved.getId());
            return productMapper.toDetailResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating product", ex);
            throw new ProductException(ErrorCode.OPERATION_FAILED, "Error creating product", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductById(Long productId) {
        var product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND, "ID: " + productId));
        return productMapper.toDetailResponse(product);
    }

    @Override
    @Transactional
    public ProductDetailResponse updateProduct(Long productId, CreateProductRequest request) {
        var product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND, "ID: " + productId));

        productMapper.updateFromRequest(request, product);
        var updated = productRepository.save(product);
        log.info("Product updated: {}", productId);
        return productMapper.toDetailResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_FOUND, "ID: " + productId);
        }
        productRepository.deleteById(productId);
        log.info("Product deleted: {}", productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ProductException(ErrorCode.INVALID_REQUEST, "Search keyword cannot be empty");
        }
        return productRepository.findByNameContainingIgnoreCase(keyword)
            .stream()
            .map(productMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findLowStockProducts()
            .stream()
            .map(productMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDetailResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndAvailable(categoryId, pageable)
            .map(productMapper::toDetailResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDetailResponse> getNewProducts(int limit) {
        return productRepository.findNewProducts(org.springframework.data.domain.PageRequest.of(0, limit))
            .stream()
            .map(productMapper::toDetailResponse)
            .toList();
    }

    @Override
    @Transactional
    public void incrementProductViews(Long productId) {
        productRepository.findById(productId).ifPresent(product -> {
            product.setViewsCount((product.getViewsCount() != null ? product.getViewsCount() : 0) + 1);
            productRepository.save(product);
        });
    }
}
