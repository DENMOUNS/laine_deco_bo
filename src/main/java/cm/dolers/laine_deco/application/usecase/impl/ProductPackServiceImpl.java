package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductPackEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductPackProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductPackRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductRepository;
import cm.dolers.laine_deco.application.dto.CreateProductPackRequest;
import cm.dolers.laine_deco.application.dto.ProductPackResponse;
import cm.dolers.laine_deco.application.mapper.ProductPackMapper;
import cm.dolers.laine_deco.application.usecase.ProductPackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductPackServiceImpl implements ProductPackService {
    private final ProductPackRepository productPackRepository;
    private final ProductRepository productRepository;
    private final ProductPackMapper mapper;

    @Override
    public ProductPackResponse createProductPack(CreateProductPackRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Pack must contain at least 1 product");
        }
        if (request.getItems().size() > 4) {
            throw new IllegalArgumentException("Pack can contain maximum 4 different products");
        }

        ProductPackEntity pack = new ProductPackEntity();
        pack.setName(request.getName());
        pack.setDescription(request.getDescription());
        pack.setPrice(request.getPrice());
        pack.setSalePrice(request.getSalePrice());
        pack.setImage(request.getImage());
        pack.setCreatedAt(Instant.now());
        pack.setUpdatedAt(Instant.now());

        Set<ProductPackProductEntity> packProducts = new HashSet<>();
        for (CreateProductPackRequest.PackProductItem item : request.getItems()) {
            var product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));
            
            ProductPackProductEntity packProduct = new ProductPackProductEntity();
            packProduct.setProductPack(pack);
            packProduct.setProduct(product);
            packProduct.setQuantity(item.getQuantity());
            packProducts.add(packProduct);
        }

        pack.setPackProducts(packProducts);
        ProductPackEntity savedPack = productPackRepository.save(pack);
        return mapper.toResponse(savedPack);
    }

    @Override
    public ProductPackResponse updateProductPack(Long packId, CreateProductPackRequest request) {
        ProductPackEntity pack = productPackRepository.findById(packId)
            .orElseThrow(() -> new IllegalArgumentException("Pack not found: " + packId));

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            if (request.getItems().size() > 4) {
                throw new IllegalArgumentException("Pack can contain maximum 4 different products");
            }

            pack.getPackProducts().clear();
            Set<ProductPackProductEntity> packProducts = new HashSet<>();
            
            for (CreateProductPackRequest.PackProductItem item : request.getItems()) {
                var product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));
                
                ProductPackProductEntity packProduct = new ProductPackProductEntity();
                packProduct.setProductPack(pack);
                packProduct.setProduct(product);
                packProduct.setQuantity(item.getQuantity());
                packProducts.add(packProduct);
            }

            pack.setPackProducts(packProducts);
        }

        pack.setName(request.getName());
        pack.setDescription(request.getDescription());
        pack.setPrice(request.getPrice());
        pack.setSalePrice(request.getSalePrice());
        pack.setImage(request.getImage());
        pack.setUpdatedAt(Instant.now());

        ProductPackEntity updatedPack = productPackRepository.save(pack);
        return mapper.toResponse(updatedPack);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPackResponse getProductPackById(Long packId) {
        ProductPackEntity pack = productPackRepository.findById(packId)
            .orElseThrow(() -> new IllegalArgumentException("Pack not found: " + packId));
        return mapper.toResponse(pack);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPackResponse> getAllActivePacks() {
        return mapper.toResponseList(productPackRepository.findAllByIsActiveTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPackResponse> getAllPacks() {
        return mapper.toResponseList(productPackRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPackResponse> searchPacksByName(String search) {
        return mapper.toResponseList(productPackRepository.searchByName(search));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPackResponse> getPacksWithPromotions() {
        return mapper.toResponseList(productPackRepository.findActivePacksWithPromotion());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPackResponse> getPacksContainingProduct(Long productId) {
        return mapper.toResponseList(productPackRepository.findPacksContainingProduct(productId));
    }

    @Override
    public void deactivateProductPack(Long packId) {
        ProductPackEntity pack = productPackRepository.findById(packId)
            .orElseThrow(() -> new IllegalArgumentException("Pack not found: " + packId));
        pack.setIsActive(false);
        pack.setUpdatedAt(Instant.now());
        productPackRepository.save(pack);
    }

    @Override
    public void activateProductPack(Long packId) {
        ProductPackEntity pack = productPackRepository.findById(packId)
            .orElseThrow(() -> new IllegalArgumentException("Pack not found: " + packId));
        pack.setIsActive(true);
        pack.setUpdatedAt(Instant.now());
        productPackRepository.save(pack);
    }

    @Override
    public void deleteProductPack(Long packId) {
        ProductPackEntity pack = productPackRepository.findById(packId)
            .orElseThrow(() -> new IllegalArgumentException("Pack not found: " + packId));
        productPackRepository.delete(pack);
    }

    @Override
    public ProductPackResponse applyPromotionToPack(Long packId, Integer discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        ProductPackEntity pack = productPackRepository.findById(packId)
            .orElseThrow(() -> new IllegalArgumentException("Pack not found: " + packId));
        
        pack.setPromotionalDiscount(discountPercentage);
        pack.setUpdatedAt(Instant.now());
        ProductPackEntity updatedPack = productPackRepository.save(pack);
        return mapper.toResponse(updatedPack);
    }

    @Override
    public ProductPackResponse removePromotionFromPack(Long packId) {
        ProductPackEntity pack = productPackRepository.findById(packId)
            .orElseThrow(() -> new IllegalArgumentException("Pack not found: " + packId));
        
        pack.setPromotionalDiscount(0);
        pack.setUpdatedAt(Instant.now());
        ProductPackEntity updatedPack = productPackRepository.save(pack);
        return mapper.toResponse(updatedPack);
    }
}
