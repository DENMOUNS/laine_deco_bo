package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.CommunityPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostJpaRepository extends JpaRepository<CommunityPostEntity, Long> {

    org.springframework.data.domain.Page<cm.dolers.laine_deco.infrastructure.persistence.entity.CommunityPostEntity> findByUserId(Long userId, org.springframework.data.domain.Pageable pageable);
}

