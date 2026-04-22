package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderEntity;
import cm.dolers.laine_deco.domain.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(Long userId);

    List<OrderEntity> findByUserIdAndStatus(Long userId, OrderStatus status);

    List<OrderEntity> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);

    List<OrderEntity> findByUserIdOrderByOrderDateDesc(Long userId);

    Long countByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    Long countByOrderDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT o FROM OrderEntity o WHERE o.user.id = :userId ORDER BY o.orderDate DESC")
    Page<OrderEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
