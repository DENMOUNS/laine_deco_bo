package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.WoolCalculationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WoolCalculationJpaRepository extends JpaRepository<WoolCalculationEntity, Long> {

    @Query("SELECT w FROM WoolCalculationEntity w WHERE w.user.id = :userId ORDER BY w.createdAt DESC")
    Page<WoolCalculationEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);
}

