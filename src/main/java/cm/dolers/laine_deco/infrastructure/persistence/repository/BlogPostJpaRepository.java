package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.BlogPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BlogPostJpaRepository extends JpaRepository<BlogPostEntity, Long> {
    Optional<BlogPostEntity> findBySlug(String slug);
    Page<BlogPostEntity> findByIsActiveTrue(Pageable pageable);
    Page<BlogPostEntity> findByPublishedTrue(Pageable pageable);
}
