package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ReviewEntity;
import cm.dolers.laine_deco.domain.model.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByProductId(Long productId);

    List<ReviewEntity> findByUserId(Long userId);

    List<ReviewEntity> findByStatus(ReviewStatus status);

    List<ReviewEntity> findByProductIdAndStatus(Long productId, ReviewStatus status);

    long countByStatus(ReviewStatus status);

    @Query("SELECT r FROM ReviewEntity r WHERE r.product.id = :productId ORDER BY r.createdAt DESC")
    Page<ReviewEntity> findByProductId(@Param("productId") Long productId, Pageable pageable);
    
    @Query("SELECT r FROM ReviewEntity r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    Page<ReviewEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);
}


