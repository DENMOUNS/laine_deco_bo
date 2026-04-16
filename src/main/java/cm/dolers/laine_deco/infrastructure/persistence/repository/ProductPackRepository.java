package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductPackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductPackRepository extends JpaRepository<ProductPackEntity, Long> {
    List<ProductPackEntity> findAllByIsActiveTrue();

    @Query("SELECT pp FROM ProductPackEntity pp WHERE pp.isActive = true AND pp.promotionalDiscount > 0 ORDER BY pp.promotionalDiscount DESC")
    List<ProductPackEntity> findActivePacksWithPromotion();

    @Query("SELECT pp FROM ProductPackEntity pp WHERE pp.isActive = true AND LOWER(pp.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<ProductPackEntity> searchByName(@Param("search") String search);

    @Query("SELECT pp FROM ProductPackEntity pp WHERE pp.isActive = false")
    List<ProductPackEntity> findAllInactivePacks();

    @Query("SELECT pp FROM ProductPackEntity pp JOIN pp.packProducts ppp WHERE ppp.product.id = :productId AND pp.isActive = true")
    List<ProductPackEntity> findPacksContainingProduct(@Param("productId") Long productId);
}
