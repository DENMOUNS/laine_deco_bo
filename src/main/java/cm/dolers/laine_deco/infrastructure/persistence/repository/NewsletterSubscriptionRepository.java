package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.NewsletterSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NewsletterSubscriptionRepository extends JpaRepository<NewsletterSubscriptionEntity, Long> {
    Optional<NewsletterSubscriptionEntity> findByEmail(String email);

    List<NewsletterSubscriptionEntity> findAllByIsActiveTrue();

    @Query("SELECT n FROM NewsletterSubscriptionEntity n WHERE n.isActive = true AND LOWER(n.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<NewsletterSubscriptionEntity> searchByEmail(@Param("search") String search);

    Long countByIsActiveTrue();
}
