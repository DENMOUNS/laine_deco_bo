package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserId(Long userId);

    List<NotificationEntity> findByUserIdAndIsReadFalse(Long userId);

    Long countByUserIdAndIsReadFalse(Long userId);

    void deleteByCreatedAtBefore(java.time.Instant date);

    org.springframework.data.domain.Page<NotificationEntity> findByUserId(Long userId, org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.user.id = :userId")
    void markAllAsReadForUser(@org.springframework.data.repository.query.Param("userId") Long userId);

    org.springframework.data.domain.Page<NotificationEntity> findByUserIdAndType(Long userId, cm.dolers.laine_deco.domain.model.NotificationType type, org.springframework.data.domain.Pageable pageable);
}




