package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.RMAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RMAJpaRepository extends JpaRepository<RMAEntity, Long> {

    java.util.Optional<RMAEntity> findByRmaNumber(String rmaNumber);
    
    @Query("SELECT r FROM RMAEntity r WHERE r.order.user.id = :userId")
    Page<RMAEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);
}

