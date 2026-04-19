package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.LoyaltyRedemption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoyaltyRedemptionRepository extends JpaRepository<LoyaltyRedemption, Long> {
    List<LoyaltyRedemption> findByUserId(Long userId);
    
    Page<LoyaltyRedemption> findByUserId(Long userId, Pageable pageable);
    
    Page<LoyaltyRedemption> findByUserIdAndStatus(Long userId, LoyaltyRedemption.RedemptionStatus status, Pageable pageable);
}
