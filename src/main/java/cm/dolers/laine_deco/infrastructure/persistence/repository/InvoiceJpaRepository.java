package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Long> {

    java.util.Optional<cm.dolers.laine_deco.infrastructure.persistence.entity.InvoiceEntity> findByInvoiceNumber(String number);
    java.util.Optional<cm.dolers.laine_deco.infrastructure.persistence.entity.InvoiceEntity> findByOrderId(Long id);
}

