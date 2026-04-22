package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.KnittingProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnittingProjectJpaRepository extends JpaRepository<KnittingProjectEntity, Long> {

    @Query("SELECT p FROM KnittingProjectEntity p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    Page<KnittingProjectEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);

    List<KnittingProjectEntity> findByUserIdAndStatus(Long userId, String status);

    @Query("SELECT p FROM KnittingProjectEntity p WHERE p.status = :status ORDER BY p.createdAt DESC")
    Page<KnittingProjectEntity> findByStatus(@Param("status") String status, Pageable pageable);
}



