package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.KnittingProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnittingProjectJpaRepository extends JpaRepository<KnittingProjectEntity, Long> {

    Page<KnittingProjectEntity> findByUserId(Long userId, Pageable pageable);

    java.util.List<KnittingProjectEntity> findByUserIdAndStatus(Long userId, String status);

    org.springframework.data.domain.Page<KnittingProjectEntity> findByStatus(String status, org.springframework.data.domain.Pageable pageable);
}



