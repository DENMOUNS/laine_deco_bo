package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.domain.model.ExpenseCategory;
import cm.dolers.laine_deco.domain.model.ExpenseStatus;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, Long> {
    List<ExpenseEntity> findByUserId(Long userId);

    List<ExpenseEntity> findByUserIdAndStatus(Long userId, ExpenseStatus status);

    List<ExpenseEntity> findByUserIdAndCategory(Long userId, ExpenseCategory category);

    List<ExpenseEntity> findByUserIdAndOperationDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<ExpenseEntity> findByUserIdAndCategoryAndOperationDateBetween(Long userId, ExpenseCategory category,
            LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.user.id = :userId AND e.status = :status")
    BigDecimal sumByUserIdAndStatus(@Param("userId") Long userId, @Param("status") ExpenseStatus status);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.user.id = :userId AND e.category = :category")
    BigDecimal sumByUserIdAndCategory(@Param("userId") Long userId, @Param("category") ExpenseCategory category);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.user.id = :userId AND e.operationDate BETWEEN :startDate AND :endDate")
    BigDecimal sumByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
