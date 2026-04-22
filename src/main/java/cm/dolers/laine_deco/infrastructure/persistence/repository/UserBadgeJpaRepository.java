package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.domain.model.BadgeType;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserBadgeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBadgeJpaRepository extends JpaRepository<UserBadgeEntity, Long> {
    List<UserBadgeEntity> findByUserId(Long userId);
    
    @Query("SELECT ub FROM UserBadgeEntity ub WHERE ub.user.id = :userId ORDER BY ub.earnedAt DESC")
    Page<UserBadgeEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    Optional<UserBadgeEntity> findByUserIdAndBadgeType(Long userId, BadgeType badgeType);
    
    boolean existsByUserIdAndBadgeType(Long userId, BadgeType badgeType);
}

