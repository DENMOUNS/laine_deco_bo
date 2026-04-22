package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.ChatService;
import cm.dolers.laine_deco.infrastructure.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Client Controller pour le Chat
 * Responsabilité: Gérer les conversations et messages d'un client avec support
 */
@RestController
@RequestMapping("/api/client/chat")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientChatController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientChatController.class);
    private final ChatService chatService;

    @PostMapping("/conversations")
    public ResponseEntity<ChatConversationResponse> createConversation() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("POST /api/client/chat/conversations - User: {}", userId);
        var response = chatService.createConversation(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/conversations/{id}")
    public ResponseEntity<ChatConversationResponse> getConversation(@PathVariable Long id) {
        log.info("GET /api/client/chat/conversations/{}", id);
        var response = chatService.getConversationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations")
    public ResponseEntity<Page<ChatConversationResponse>> getMyConversations(Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("GET /api/client/chat/conversations - User: {}", userId);
        var response = chatService.getClientConversations(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations/{id}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long id) {
        log.info("GET /api/client/chat/conversations/{}/unread-count", id);
        var count = chatService.getUnreadMessageCount(id);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/conversations/{id}/messages")
    public ResponseEntity<ChatMessageResponse> sendMessage(@PathVariable Long id,
            @Valid @RequestBody CreateChatMessageRequest request) {
        log.info("POST /api/client/chat/conversations/{}/messages", id);
        var modifiedRequest = new CreateChatMessageRequest(id, request.message(), "USER");
        var response = chatService.sendMessage(modifiedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<Page<ChatMessageResponse>> getConversationMessages(@PathVariable Long id, Pageable pageable) {
        log.info("GET /api/client/chat/conversations/{}/messages", id);
        var response = chatService.getConversationMessages(id, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/messages/{messageId}/mark-as-read")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId) {
        log.info("POST /api/client/chat/messages/{}/mark-as-read", messageId);
        chatService.markMessageAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/conversations/{id}/mark-all-read")
    public ResponseEntity<Void> markAllMessagesAsRead(@PathVariable Long id) {
        log.info("POST /api/client/chat/conversations/{}/mark-all-read", id);
        chatService.markAllMessagesAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/conversations/{id}/close")
    public ResponseEntity<Void> closeConversation(@PathVariable Long id) {
        log.info("POST /api/client/chat/conversations/{}/close", id);
        chatService.closeConversation(id);
        return ResponseEntity.ok().build();
    }
}
