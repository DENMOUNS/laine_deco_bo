package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interface Service pour Chat
 * Gestion des conversations et messages bidirectionnels client/admin/finance
 */
public interface ChatService {
    // ==================== Conversations ====================
    
    /**
     * Créer une conversation (client initie)
     */
    ChatConversationResponse createConversation(Long clientId);
    
    /**
     * Obtenir une conversation par ID
     */
    ChatConversationResponse getConversationById(Long conversationId);
    
    /**
     * Récupérer les conversations d'un client
     */
    Page<ChatConversationResponse> getClientConversations(Long clientId, Pageable pageable);
    
    /**
     * Récupérer toutes les conversations non fermées (admin/finance)
     */
    Page<ChatConversationResponse> getOpenConversations(Pageable pageable);
    
    /**
     * Assigner une conversation à un agent (admin/finance)
     */
    ChatConversationResponse assignConversation(Long conversationId, Long agentId);
    
    /**
     * Changer le statut d'une conversation
     */
    ChatConversationResponse updateConversationStatus(Long conversationId, String newStatus);
    
    /**
     * Fermer une conversation
     */
    void closeConversation(Long conversationId);
    
    // ==================== Messages ====================
    
    /**
     * Envoyer un message (user, admin, ou finance)
     */
    ChatMessageResponse sendMessage(CreateChatMessageRequest request);
    
    /**
     * Envoyer une réponse admin/finance (avec notification automatique)
     */
    ChatMessageResponse sendAdminReply(CreateChatMessageRequest request);
    
    /**
     * Récupérer les messages d'une conversation
     */
    Page<ChatMessageResponse> getConversationMessages(Long conversationId, Pageable pageable);
    
    /**
     * Marquer un message comme lu
     */
    void markMessageAsRead(Long messageId);
    
    /**
     * Marquer tous les messages d'une conversation comme lus
     */
    void markAllMessagesAsRead(Long conversationId);
    
    /**
     * Récupérer le nombre de messages non lus pour une conversation
     */
    Long getUnreadMessageCount(Long conversationId);
    
    /**
     * Récupérer le nombre total de messages non lus pour l'utilisateur
     */
    Long getTotalUnreadMessages(Long userId);
}
