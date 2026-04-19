package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.PromoEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoEventJpaRepository extends JpaRepository<PromoEventEntity, Long> {

    org.springframework.data.domain.Page<cm.dolers.laine_deco.infrastructure.persistence.entity.PromoEventEntity> findByStatusAndEndDateAfter(String status, java.time.Instant date, org.springframework.data.domain.Pageable pageable);
}

