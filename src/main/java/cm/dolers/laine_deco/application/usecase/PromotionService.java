package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.PromotionResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service pour gérer les promotions et calculer les prix réduits
 */
public interface PromotionService {

    /**
     * Récupère toutes les promotions actuellement actives
     */
    List<PromotionResponse> getActivePromotions();

    /**
     * Récupère les promotions actives pour une catégorie spécifique
     */
    List<PromotionResponse> getPromotionForCategory(Long categoryId);

    /**
     * Récupère les promotions actives pour une marque spécifique
     */
    List<PromotionResponse> getPromotionForBrand(String brand);

    /**
     * Récupère les promotions actives pour un produit spécifique
     */
    List<PromotionResponse> getPromotionForProduct(Long productId);

    /**
     * Récupère les ventes flash actuelles
     */
    List<PromotionResponse> getActiveFlashSales();

    /**
     * Calcule le prix réduit d'un produit en fonction des promotions actives
     * Retourne le prix d'origine si aucune promotion n'est applicable
     */
    BigDecimal calculateReducedPrice(ProductEntity product);

    /**
     * Récupère le pourcentage de réduction applicable pour un produit
     */
    BigDecimal getDiscountPercentage(ProductEntity product);

    /**
     * Crée une nouvelle promotion
     */
    PromotionResponse createPromotion(String name, String description, String type,
                                      java.time.Instant startDate, java.time.Instant endDate,
                                      BigDecimal discountPercentage, BigDecimal discountAmount,
                                      Long categoryId, String brand, Long productId);

    /**
     * Met à jour une promotion
     */
    PromotionResponse updatePromotion(Long promotionId, String name, String description,
                                      java.time.Instant startDate, java.time.Instant endDate,
                                      BigDecimal discountPercentage, BigDecimal discountAmount);

    /**
     * Désactive une promotion
     */
    void deactivatePromotion(Long promotionId);

    /**
     * Met automatiquement à jour le statut des promotions expirées
     */
    void deactivateExpiredPromotions();
}
