package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface Service pour Chat
 */
public interface ChatService {
    ChatConversationResponse createConversation(Long clientId);
    ChatConversationResponse getConversationById(Long conversationId);
    Page<ChatConversationResponse> getClientConversations(Long clientId, Pageable pageable);
    ChatMessageResponse sendMessage(CreateChatMessageRequest request);
    Page<ChatMessageResponse> getConversationMessages(Long conversationId, Pageable pageable);
    void markMessageAsRead(Long messageId);
    void closeConversation(Long conversationId);
    Long getUnreadMessageCount(Long conversationId);

    Page<ChatConversationResponse> getOpenConversations(Pageable pageable);
    ChatConversationResponse assignConversation(Long conversationId, Long agentId);

    org.springframework.data.domain.Page<ChatConversationResponse> getUserConversations(Long userId, org.springframework.data.domain.Pageable pageable);

    void markAllMessagesAsRead(Long conversationId);

    ChatConversationResponse updateConversationStatus(Long conversationId, String status);

    cm.dolers.laine_deco.application.dto.ChatMessageResponse sendAdminReply(cm.dolers.laine_deco.application.dto.CreateChatMessageRequest request);
}













