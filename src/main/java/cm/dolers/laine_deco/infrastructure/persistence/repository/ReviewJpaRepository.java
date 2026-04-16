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
}
