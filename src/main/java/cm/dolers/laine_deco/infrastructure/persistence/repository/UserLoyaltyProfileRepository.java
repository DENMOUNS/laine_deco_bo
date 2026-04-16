package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.UserLoyaltyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoyaltyProfileRepository extends JpaRepository<UserLoyaltyProfile, Long> {
    Optional<UserLoyaltyProfile> findByUserId(Long userId);
}
