package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ConversationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationJpaRepository extends JpaRepository<ConversationEntity, Long> {
    Page<ConversationEntity> findByClientId(Long clientId, Pageable pageable);
    Page<ConversationEntity> findByStatusIn(String[] statuses, Pageable pageable);

    long countByStatus(String status);
}

