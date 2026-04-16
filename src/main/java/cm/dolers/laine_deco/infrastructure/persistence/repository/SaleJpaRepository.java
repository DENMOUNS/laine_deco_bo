package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleJpaRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByProductId(Long productId);

    List<SaleEntity> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(s.totalAmount) FROM SaleEntity s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal sumRevenueBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT s FROM SaleEntity s WHERE s.product.id = :productId ORDER BY s.saleDate DESC")
    List<SaleEntity> findByProductIdOrderByDateDesc(@Param("productId") Long productId);

    @Query("SELECT SUM(s.quantity) FROM SaleEntity s WHERE s.product.id = :productId")
    Integer sumQuantityByProductId(@Param("productId") Long productId);
}
