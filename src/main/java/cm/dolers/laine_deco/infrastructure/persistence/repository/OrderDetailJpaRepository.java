package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailJpaRepository extends JpaRepository<OrderDetailEntity, Long> {

    java.util.List<cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity> findByOrderId(Long orderId);
}

