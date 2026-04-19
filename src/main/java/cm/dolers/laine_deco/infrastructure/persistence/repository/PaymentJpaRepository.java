package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

    java.util.Optional<cm.dolers.laine_deco.infrastructure.persistence.entity.PaymentEntity> findByTransactionId(String transactionId);

    java.util.List<cm.dolers.laine_deco.infrastructure.persistence.entity.PaymentEntity> findByOrderId(Long orderId);
}


