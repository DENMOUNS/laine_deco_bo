package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.PromoEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface PromoEventJpaRepository extends JpaRepository<PromoEventEntity, Long> {

    @Query("SELECT p FROM PromoEventEntity p WHERE p.status = :status AND p.endDate > :date ORDER BY p.startDate DESC")
    Page<PromoEventEntity> findByStatusAndEndDateAfter(@Param("status") String status, @Param("date") Instant date, Pageable pageable);
}

