package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.MaintenanceGuideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaintenanceGuideRepository extends JpaRepository<MaintenanceGuideEntity, Long> {
    List<MaintenanceGuideEntity> findAllByIsActiveTrueOrderByCreatedAtDesc();

    @Query("SELECT m FROM MaintenanceGuideEntity m WHERE m.isActive = true AND m.category.id = :categoryId")
    List<MaintenanceGuideEntity> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT m FROM MaintenanceGuideEntity m WHERE m.isActive = true AND m.brand = :brand")
    List<MaintenanceGuideEntity> findByBrand(@Param("brand") String brand);

    @Query("SELECT m FROM MaintenanceGuideEntity m WHERE m.isActive = true AND m.product.id = :productId")
    List<MaintenanceGuideEntity> findByProductId(@Param("productId") Long productId);

    @Query("SELECT m FROM MaintenanceGuideEntity m WHERE m.isActive = true AND LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<MaintenanceGuideEntity> searchByTitle(@Param("search") String search);

    List<MaintenanceGuideEntity> findAllByIsActiveFalse();
}
