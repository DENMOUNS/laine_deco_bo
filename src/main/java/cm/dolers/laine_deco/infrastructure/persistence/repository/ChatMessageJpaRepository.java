package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {
    Page<ChatMessageEntity> findByConversationId(Long conversationId, Pageable pageable);
    List<ChatMessageEntity> findByConversationId(Long conversationId);
    Long countByConversationIdAndIsReadFalse(Long conversationId);
    
    @Query("SELECT COUNT(m) FROM ChatMessageEntity m WHERE m.conversation.client.id = :userId AND m.isRead = false AND m.senderType != 'CLIENT'")
    Long countUnreadMessagesForUser(@Param("userId") Long userId);
}
