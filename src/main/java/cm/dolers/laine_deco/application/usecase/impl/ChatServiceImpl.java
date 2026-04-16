package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ChatMapper;
import cm.dolers.laine_deco.application.usecase.ChatService;
import cm.dolers.laine_deco.application.usecase.NotificationService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.UserException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final ChatConversationJpaRepository conversationRepository;
    private final ChatMessageJpaRepository messageRepository;
    private final UserJpaRepository userRepository;
    private final ChatMapper chatMapper;
    private final NotificationService notificationService;

    // ==================== Conversations ====================

    @Override
    @Transactional
    public ChatConversationResponse createConversation(Long clientId) {
        log.info("Creating chat conversation for client: {}", clientId);
        
        var user = userRepository.findById(clientId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "ID: " + clientId));

        try {
            var conversation = new ChatConversationEntity();
            conversation.setClient(user);
            conversation.setStatus("OPEN");
            conversation.setUnreadCount(0);

            var saved = conversationRepository.save(conversation);
            log.info("Conversation created: {}", saved.getId());
            return chatMapper.toConversationResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating conversation", ex);
            throw new UserException(ErrorCode.OPERATION_FAILED, "Error creating conversation", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ChatConversationResponse getConversationById(Long conversationId) {
        var conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new UserException(ErrorCode.CHAT_NOT_FOUND, "ID: " + conversationId));
        return chatMapper.toConversationResponse(conversation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatConversationResponse> getClientConversations(Long clientId, Pageable pageable) {
        return conversationRepository.findByClientId(clientId, pageable)
            .map(chatMapper::toConversationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatConversationResponse> getOpenConversations(Pageable pageable) {
        log.info("Fetching open conversations");
        return conversationRepository.findByStatusIn(new String[]{"OPEN", "PENDING", "IN_PROGRESS"}, pageable)
            .map(chatMapper::toConversationResponse);
    }

    @Override
    @Transactional
    public ChatConversationResponse assignConversation(Long conversationId, Long agentId) {
        log.info("Assigning conversation {} to agent {}", conversationId, agentId);

        var conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new UserException(ErrorCode.CHAT_NOT_FOUND, "ID: " + conversationId));

        var agent = userRepository.findById(agentId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "Agent ID: " + agentId));

        conversation.setAssignedAgent(agent);
        conversation.setStatus("IN_PROGRESS");
        
        var saved = conversationRepository.save(conversation);
        log.info("Conversation assigned successfully");
        return chatMapper.toConversationResponse(saved);
    }

    @Override
    @Transactional
    public ChatConversationResponse updateConversationStatus(Long conversationId, String newStatus) {
        log.info("Updating conversation {} status to {}", conversationId, newStatus);

        var conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new UserException(ErrorCode.CHAT_NOT_FOUND, "ID: " + conversationId));

        conversation.setStatus(newStatus);
        if ("RESOLVED".equals(newStatus)) {
            conversation.setResolvedAt(Instant.now());
        }
        if ("CLOSED".equals(newStatus)) {
            conversation.setClosedAt(Instant.now());
        }

        var saved = conversationRepository.save(conversation);
        return chatMapper.toConversationResponse(saved);
    }

    @Override
    @Transactional
    public void closeConversation(Long conversationId) {
        log.info("Closing conversation: {}", conversationId);
        var conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new UserException(ErrorCode.CHAT_NOT_FOUND, "ID: " + conversationId));
        
        conversation.setStatus("CLOSED");
        conversation.setClosedAt(Instant.now());
        conversationRepository.save(conversation);
    }

    // ==================== Messages ====================

    @Override
    @Transactional
    public ChatMessageResponse sendMessage(CreateChatMessageRequest request) {
        log.info("Sending message to conversation: {}", request.conversationId());

        var conversation = conversationRepository.findById(request.conversationId())
            .orElseThrow(() -> new UserException(ErrorCode.CHAT_NOT_FOUND, "ID: " + request.conversationId()));

        try {
            var message = new ChatMessageEntity();
            message.setConversation(conversation);
            message.setMessage(request.message());
            message.setSenderType(request.senderType());
            message.setIsRead(false);

            var saved = messageRepository.save(message);
            log.info("Message sent: {}", saved.getId());
            
            // Mettre à jour le timestamp de dernier message
            conversation.setLastMessageAt(Instant.now());
            conversationRepository.save(conversation);

            return chatMapper.toMessageResponse(saved);
        } catch (Exception ex) {
            log.error("Error sending message", ex);
            throw new UserException(ErrorCode.OPERATION_FAILED, "Error sending message", ex);
        }
    }

    @Override
    @Transactional
    public ChatMessageResponse sendAdminReply(CreateChatMessageRequest request) {
        log.info("Admin sending reply to conversation: {}", request.conversationId());

        var conversation = conversationRepository.findById(request.conversationId())
            .orElseThrow(() -> new UserException(ErrorCode.CHAT_NOT_FOUND, "ID: " + request.conversationId()));

        try {
            var message = new ChatMessageEntity();
            message.setConversation(conversation);
            message.setMessage(request.message());
            message.setSenderType(request.senderType());  // ADMIN ou FINANCE
            message.setIsRead(false);

            var saved = messageRepository.save(message);
            log.info("Admin reply sent: {}", saved.getId());
            
            // Marquer la conversation comme IN_PROGRESS si elle était en attente
            if ("PENDING".equals(conversation.getStatus()) || "OPEN".equals(conversation.getStatus())) {
                conversation.setStatus("IN_PROGRESS");
            }
            
            conversation.setLastMessageAt(Instant.now());
            conversationRepository.save(conversation);

            // Créer automatiquement une notification de réponse pour le client
            var clientNotif = new ChatReplyNotificationRequest(
                conversation.getId(),
                saved.getSenderId(),
                saved.getSenderName(),
                request.senderType(),
                request.message(),
                true  // isFromAdmin
            );
            notificationService.notifyOnChatReply(clientNotif);

            return chatMapper.toMessageResponse(saved);
        } catch (Exception ex) {
            log.error("Error sending admin reply", ex);
            throw new UserException(ErrorCode.OPERATION_FAILED, "Error sending admin reply", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getConversationMessages(Long conversationId, Pageable pageable) {
        return messageRepository.findByConversationId(conversationId, pageable)
            .map(chatMapper::toMessageResponse);
    }

    @Override
    @Transactional
    public void markMessageAsRead(Long messageId) {
        var message = messageRepository.findById(messageId)
            .orElseThrow(() -> new UserException(ErrorCode.CHAT_MSG_NOT_FOUND, "ID: " + messageId));
        message.setIsRead(true);
        message.setReadAt(Instant.now());
        messageRepository.save(message);
        log.info("Message marked as read: {}", messageId);
    }

    @Override
    @Transactional
    public void markAllMessagesAsRead(Long conversationId) {
        log.info("Marking all messages as read for conversation: {}", conversationId);
        var messages = messageRepository.findByConversationId(conversationId);
        messages.forEach(msg -> {
            msg.setIsRead(true);
            msg.setReadAt(Instant.now());
        });
        messageRepository.saveAll(messages);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadMessageCount(Long conversationId) {
        return messageRepository.countByConversationIdAndIsReadFalse(conversationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalUnreadMessages(Long userId) {
        return messageRepository.countUnreadMessagesForUser(userId);
    }
}
    }

    @Override
    @Transactional
    public void closeConversation(Long conversationId) {
        var conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new UserException(ErrorCode.CHAT_NOT_FOUND, "ID: " + conversationId));
        conversation.setStatus("CLOSED");
        conversationRepository.save(conversation);
        log.info("Conversation closed: {}", conversationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadMessageCount(Long conversationId) {
        return messageRepository.countByConversationIdAndIsReadFalse(conversationId);
    }
}
