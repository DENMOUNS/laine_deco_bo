package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.CommunityPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostJpaRepository extends JpaRepository<CommunityPostEntity, Long> {

    @Query("SELECT p FROM CommunityPostEntity p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    Page<CommunityPostEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);
}

