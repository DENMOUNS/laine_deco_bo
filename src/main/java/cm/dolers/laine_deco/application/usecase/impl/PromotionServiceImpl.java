package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.PromotionResponse;
import cm.dolers.laine_deco.application.mapper.PromotionMapper;
import cm.dolers.laine_deco.application.usecase.PromotionService;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PromotionEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.PromotionRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.CategoryJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import cm.dolers.laine_deco.domain.model.PromotionType;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

@Transactional(readOnly = true)
public class PromotionServiceImpl implements PromotionService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PromotionServiceImpl.class);
    private final PromotionRepository promotionRepository;
    private final CategoryJpaRepository CategoryJpaRepository;
    private final ProductJpaRepository ProductJpaRepository;
    private final PromotionMapper promotionMapper;

    @Override
    public List<PromotionResponse> getActivePromotions() {
        log.info("Fetching all active promotions");
        return promotionRepository.findActivePromotions(Instant.now()).stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionResponse> getPromotionForCategory(Long categoryId) {
        log.info("Fetching promotions for category: {}", categoryId);
        return promotionRepository.findActiveByCategoryId(categoryId, Instant.now()).stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionResponse> getPromotionForBrand(String brand) {
        log.info("Fetching promotions for brand: {}", brand);
        return promotionRepository.findActiveByBrand(brand, Instant.now()).stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionResponse> getPromotionForProduct(Long productId) {
        log.info("Fetching promotions for product: {}", productId);
        return promotionRepository.findActiveByProductId(productId, Instant.now()).stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionResponse> getActiveFlashSales() {
        log.info("Fetching active flash sales");
        return promotionRepository.findActiveFlashSales(Instant.now()).stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculateReducedPrice(ProductEntity product) {
        log.debug("Calculating reduced price for product: {}", product.getId());

        if (product == null || product.getSalePrice() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal originalPrice = product.getSalePrice();
        List<PromotionEntity> applicablePromotions = findApplicablePromotions(product);

        if (applicablePromotions.isEmpty()) {
            log.debug("No applicable promotions for product: {}", product.getId());
            return originalPrice;
        }

        // Appliquer la réduction la plus élevée
        BigDecimal maxReduction = BigDecimal.ZERO;
        for (PromotionEntity promo : applicablePromotions) {
            BigDecimal reduction = calculateReduction(originalPrice, promo);
            if (reduction.compareTo(maxReduction) > 0) {
                maxReduction = reduction;
            }
        }

        BigDecimal reducedPrice = originalPrice.subtract(maxReduction);
        log.debug("Reduced price for product {}: {} -> {}", product.getId(), originalPrice, reducedPrice);
        return reducedPrice.max(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getDiscountPercentage(ProductEntity product) {
        log.debug("Getting discount percentage for product: {}", product.getId());

        if (product == null || product.getSalePrice() == null || product.getSalePrice().signum() == 0) {
            return BigDecimal.ZERO;
        }

        List<PromotionEntity> applicablePromotions = findApplicablePromotions(product);
        if (applicablePromotions.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Retourner le pourcentage le plus élevé
        return applicablePromotions.stream()
                .map(PromotionEntity::getDiscountPercentage)
                .filter(p -> p != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional
    public PromotionResponse createPromotion(String name, String description, String type,
            Instant startDate, Instant endDate,
            BigDecimal discountPercentage, BigDecimal discountAmount,
            Long categoryId, String brand, Long productId) {
        log.info("Creating new promotion: {} - Type: {}", name, type);

        PromotionEntity promotion = new PromotionEntity();
        promotion.setName(name);
        promotion.setDescription(description);
        promotion.setType(PromotionType.valueOf(type));
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setDiscountPercentage(discountPercentage);
        promotion.setDiscountAmount(discountAmount);
        promotion.setBrand(brand);
        promotion.setIsActive(true);

        if (categoryId != null) {
            var category = CategoryJpaRepository.findById(categoryId);
            category.ifPresent(promotion::setCategory);
        }

        if (productId != null) {
            var product = ProductJpaRepository.findById(productId);
            product.ifPresent(promotion::setProduct);
        }

        PromotionEntity saved = promotionRepository.save(promotion);
        log.info("Promotion created: {} (ID: {})", name, saved.getId());
        return promotionMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public PromotionResponse updatePromotion(Long promotionId, String name, String description,
            Instant startDate, Instant endDate,
            BigDecimal discountPercentage, BigDecimal discountAmount) {
        log.info("Updating promotion: {}", promotionId);

        PromotionEntity promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found"));

        promotion.setName(name);
        promotion.setDescription(description);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setDiscountPercentage(discountPercentage);
        promotion.setDiscountAmount(discountAmount);
        promotion.setUpdatedAt(Instant.now());

        PromotionEntity updated = promotionRepository.save(promotion);
        log.info("Promotion updated: {}", promotionId);
        return promotionMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deactivatePromotion(Long promotionId) {
        log.info("Deactivating promotion: {}", promotionId);

        PromotionEntity promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found"));

        promotion.setIsActive(false);
        promotion.setUpdatedAt(Instant.now());
        promotionRepository.save(promotion);

        log.info("Promotion deactivated: {}", promotionId);
    }

    @Override
    @Transactional
    public void deactivateExpiredPromotions() {
        log.info("Deactivating expired promotions");

        List<PromotionEntity> expiredPromotions = promotionRepository.findExpiredPromotions(Instant.now());
        for (PromotionEntity promotion : expiredPromotions) {
            promotion.setIsActive(false);
            promotion.setUpdatedAt(Instant.now());
        }
        promotionRepository.saveAll(expiredPromotions);

        log.info("Deactivated {} expired promotions", expiredPromotions.size());
    }

    /**
     * Trouve toutes les promotions applicables pour un produit
     */
    private List<PromotionEntity> findApplicablePromotions(ProductEntity product) {
        List<PromotionEntity> applicable = promotionRepository.findActivePromotions(Instant.now());

        return applicable.stream()
                .filter(promo -> isPromotionApplicable(promo, product))
                .collect(Collectors.toList());
    }

    /**
     * Vérifie si une promotion s'applique à un produit
     */
    private boolean isPromotionApplicable(PromotionEntity promotion, ProductEntity product) {
        // Vente flash sur ce produit spécifique
        if (promotion.getProduct() != null) {
            return promotion.getProduct().getId().equals(product.getId());
        }

        // Réduction sur cette catégorie
        if (promotion.getCategory() != null) {
            return product.getCategory() != null &&
                    promotion.getCategory().getId().equals(product.getCategory().getId());
        }

        // Réduction sur cette marque
        if (promotion.getBrand() != null) {
            return promotion.getBrand().equalsIgnoreCase(product.getBrand());
        }

        return false;
    }

    /**
     * Calcule le montant de réduction
     */
    private BigDecimal calculateReduction(BigDecimal originalPrice, PromotionEntity promotion) {
        if (promotion.getDiscountPercentage() != null && promotion.getDiscountPercentage().signum() > 0) {
            return originalPrice.multiply(promotion.getDiscountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (promotion.getDiscountAmount() != null && promotion.getDiscountAmount().signum() > 0) {
            return promotion.getDiscountAmount();
        }
        return BigDecimal.ZERO;
    }
}
