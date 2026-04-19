package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.CommunityCommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentJpaRepository extends JpaRepository<CommunityCommentEntity, Long> {
    Page<CommunityCommentEntity> findByPostId(Long postId, Pageable pageable);
}
