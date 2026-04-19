package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.WoolCalculationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WoolCalculationJpaRepository extends JpaRepository<WoolCalculationEntity, Long> {

    org.springframework.data.domain.Page<cm.dolers.laine_deco.infrastructure.persistence.entity.WoolCalculationEntity> findByUserId(Long userId, org.springframework.data.domain.Pageable pageable);
}

