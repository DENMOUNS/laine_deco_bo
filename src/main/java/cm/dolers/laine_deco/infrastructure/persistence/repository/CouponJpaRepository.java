package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.CouponEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {
    Optional<CouponEntity> findByCode(String code);

    boolean existsByCode(String code);

    // JPQL avec tri explicite pour éviter l'erreur sur le tri automatique
    @Query("SELECT c FROM CouponEntity c WHERE c.isActive = true AND c.expiryDate > :date ORDER BY c.expiryDate ASC")
    Page<CouponEntity> findActiveCoupons(@Param("date") Instant date, Pageable pageable);
}
