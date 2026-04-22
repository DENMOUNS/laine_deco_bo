package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ConversationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationJpaRepository extends JpaRepository<ConversationEntity, Long> {
    @Query("SELECT c FROM ConversationEntity c WHERE c.client.id = :clientId ORDER BY c.lastMessageAt DESC")
    Page<ConversationEntity> findByClientId(@Param("clientId") Long clientId, Pageable pageable);
    
    @Query("SELECT c FROM ConversationEntity c WHERE c.status IN (:statuses) ORDER BY c.lastMessageAt DESC")
    Page<ConversationEntity> findByStatusIn(@Param("statuses") String[] statuses, Pageable pageable);

    long countByStatus(String status);
}

