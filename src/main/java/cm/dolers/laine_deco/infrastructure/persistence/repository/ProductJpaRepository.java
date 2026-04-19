package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    Optional<ProductEntity> findBySku(String sku);

    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM ProductEntity p WHERE p.stockQuantity <= p.reorderLevel ORDER BY p.stockQuantity ASC")
    List<ProductEntity> findLowStockProducts();

    // Recherche texte avec pagination
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR p.sku LIKE :keyword")
    Page<ProductEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Recherche par catégorie avec pagination
    Page<ProductEntity> findByCategoryIdAndStockQuantityGreaterThan(Long categoryId, Integer stock, Pageable pageable);

    // Les produits populaires (par nombre de vues et ventes)
    @Query("SELECT p FROM ProductEntity p ORDER BY p.viewsCount DESC, p.salesCount DESC")
    Page<ProductEntity> findPopularProducts(Pageable pageable);

    // Les produits les mieux notés
    @Query("SELECT p FROM ProductEntity p WHERE p.rating >= :minRating ORDER BY p.rating DESC, p.salesCount DESC")
    Page<ProductEntity> findTopRatedProducts(@Param("minRating") Integer minRating, Pageable pageable);

    // Produits neufs (ordonnés par date de création)
    @Query("SELECT p FROM ProductEntity p ORDER BY p.createdAt DESC")
    Page<ProductEntity> findNewestProducts(Pageable pageable);

    // Auto-complétion (pour les suggestions de recherche)
    @Query(value = "SELECT DISTINCT p.name FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT(:prefix, '%')) LIMIT 10", nativeQuery = false)
    List<String> findProductNameSuggestions(@Param("prefix") String prefix);

    

    @org.springframework.data.jpa.repository.Query("SELECT p FROM ProductEntity p ORDER BY p.createdAt DESC")
    org.springframework.data.domain.Page<cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity> findNewProducts(org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM ProductEntity p WHERE p.category.id = :categoryId")
    org.springframework.data.domain.Page<cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity> findByCategoryIdAndAvailable(@org.springframework.data.repository.query.Param("categoryId") Long categoryId, org.springframework.data.domain.Pageable pageable);

    long countByCategory(cm.dolers.laine_deco.infrastructure.persistence.entity.CategoryEntity category);
}





