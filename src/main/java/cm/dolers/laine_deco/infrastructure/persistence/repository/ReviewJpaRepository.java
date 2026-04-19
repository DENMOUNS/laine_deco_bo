package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ReviewEntity;
import cm.dolers.laine_deco.domain.model.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByProductId(Long productId);

    List<ReviewEntity> findByUserId(Long userId);

    List<ReviewEntity> findByStatus(ReviewStatus status);

    List<ReviewEntity> findByProductIdAndStatus(Long productId, ReviewStatus status);

    long countByStatus(cm.dolers.laine_deco.domain.model.ReviewStatus status);

    org.springframework.data.domain.Page<cm.dolers.laine_deco.infrastructure.persistence.entity.ReviewEntity> findByProductId(Long productId, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<cm.dolers.laine_deco.infrastructure.persistence.entity.ReviewEntity> findByUserId(Long userId, org.springframework.data.domain.Pageable pageable);
}


