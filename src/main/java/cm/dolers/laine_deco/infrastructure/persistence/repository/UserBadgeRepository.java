package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.UserBadge;
import cm.dolers.laine_deco.domain.model.BadgeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findByUserId(Long userId);
    
    Page<UserBadge> findByUserId(Long userId, Pageable pageable);
    
    Optional<UserBadge> findByUserIdAndBadgeType(Long userId, BadgeType badgeType);
    
    boolean existsByUserIdAndBadgeType(Long userId, BadgeType badgeType);
}
