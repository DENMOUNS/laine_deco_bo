package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.AuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
    
    /**
     * Récupère les logs d'audit pour un utilisateur
     */
    Page<AuditLogEntity> findByUserId(Long userId, Pageable pageable);

    /**
     * Récupère les logs d'audit par action
     */
    Page<AuditLogEntity> findByAction(String action, Pageable pageable);

    /**
     * Récupère les logs d'audit pour une entité spécifique
     */
    Page<AuditLogEntity> findByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);

    /**
     * Récupère les logs dans une plage de dates
     */
    @Query("SELECT a FROM AuditLogEntity a WHERE a.timestamp BETWEEN :startTime AND :endTime ORDER BY a.timestamp DESC")
    Page<AuditLogEntity> findAuditLogsByDateRange(
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime,
        Pageable pageable);

    /**
     * Récupère les logs avec recherche combinée
     */
    @Query("SELECT a FROM AuditLogEntity a WHERE " +
           "(:userId IS NULL OR a.userId = :userId) AND " +
           "(:action IS NULL OR a.action = :action) AND " +
           "(:entityType IS NULL OR a.entityType = :entityType) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "a.timestamp BETWEEN :startTime AND :endTime " +
           "ORDER BY a.timestamp DESC")
    Page<AuditLogEntity> searchAuditLogs(
        @Param("userId") Long userId,
        @Param("action") String action,
        @Param("entityType") String entityType,
        @Param("status") String status,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime,
        Pageable pageable);

    /**
     * Récupère les échecs
     */
    @Query("SELECT a FROM AuditLogEntity a WHERE a.status = 'FAILURE' ORDER BY a.timestamp DESC")
    Page<AuditLogEntity> findFailedAuditLogs(Pageable pageable);

    /**
     * Compte les actions d'un utilisateur
     */
    Long countByUserIdAndAction(Long userId, String action);

    /**
     * Supprime les logs d'audit plus vieux que X jours
     */
    @Query("DELETE FROM AuditLogEntity a WHERE a.timestamp < :cutoffTime")
    void deleteOldAuditLogs(@Param("cutoffTime") Instant cutoffTime);
}
