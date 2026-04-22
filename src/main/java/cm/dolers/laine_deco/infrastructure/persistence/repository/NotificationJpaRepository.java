package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.NotificationEntity;
import cm.dolers.laine_deco.domain.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserId(Long userId);

    List<NotificationEntity> findByUserIdAndIsReadFalse(Long userId);

    Long countByUserIdAndIsReadFalse(Long userId);

    void deleteByCreatedAtBefore(Instant date);

    @Query("SELECT n FROM NotificationEntity n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    Page<NotificationEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.user.id = :userId")
    void markAllAsReadForUser(@Param("userId") Long userId);

    @Query("SELECT n FROM NotificationEntity n WHERE n.user.id = :userId AND n.type = :type ORDER BY n.createdAt DESC")
    Page<NotificationEntity> findByUserIdAndType(@Param("userId") Long userId, @Param("type") NotificationType type, Pageable pageable);
}




