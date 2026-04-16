package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {

    /**
     * Récupère toutes les promotions actuellement actives
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.isActive = true AND p.startDate <= :now AND p.endDate > :now ORDER BY p.endDate ASC")
    List<PromotionEntity> findActivePromotions(@Param("now") Instant now);

    /**
     * Récupère les promotions actives pour une catégorie spécifique
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.isActive = true AND p.startDate <= :now AND p.endDate > :now AND p.category.id = :categoryId")
    List<PromotionEntity> findActiveByCategoryId(@Param("categoryId") Long categoryId, @Param("now") Instant now);

    /**
     * Récupère les promotions actives pour une marque spécifique
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.isActive = true AND p.startDate <= :now AND p.endDate > :now AND p.brand = :brand")
    List<PromotionEntity> findActiveByBrand(@Param("brand") String brand, @Param("now") Instant now);

    /**
     * Récupère les promotions actives pour un produit spécifique
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.isActive = true AND p.startDate <= :now AND p.endDate > :now AND p.product.id = :productId")
    List<PromotionEntity> findActiveByProductId(@Param("productId") Long productId, @Param("now") Instant now);

    /**
     * Récupère les promotions expirées mais non explicitement désactivées
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.isActive = true AND p.endDate <= :now")
    List<PromotionEntity> findExpiredPromotions(@Param("now") Instant now);

    /**
     * Récupère les ventes flash actives (avec timer)
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.isActive = true AND p.type = 'FLASH_SALE' AND p.startDate <= :now AND p.endDate > :now ORDER BY p.endDate ASC")
    List<PromotionEntity> findActiveFlashSales(@Param("now") Instant now);

    /**
     * Récupère toutes les promotions avec pagination
     */
    List<PromotionEntity> findAllByIsActiveTrue();
}
