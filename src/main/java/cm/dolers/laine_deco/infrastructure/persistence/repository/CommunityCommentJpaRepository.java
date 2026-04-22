package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.CommunityCommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentJpaRepository extends JpaRepository<CommunityCommentEntity, Long> {
    @Query("SELECT c FROM CommunityCommentEntity c WHERE c.post.id = :postId ORDER BY c.createdAt DESC")
    Page<CommunityCommentEntity> findByPostId(@Param("postId") Long postId, Pageable pageable);
}
