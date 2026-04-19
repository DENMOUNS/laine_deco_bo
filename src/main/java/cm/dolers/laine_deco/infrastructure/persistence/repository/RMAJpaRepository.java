package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.RMAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RMAJpaRepository extends JpaRepository<RMAEntity, Long> {

    java.util.Optional<RMAEntity> findByRmaNumber(String rmaNumber);
    
    org.springframework.data.domain.Page<RMAEntity> findByOrderUserId(Long userId, org.springframework.data.domain.Pageable pageable);
}

