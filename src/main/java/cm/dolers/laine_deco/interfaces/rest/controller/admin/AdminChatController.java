package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour le Chat
 * Responsabilité: Gérer les conversations du support (assignation, réponses, statuts)
 */
@RestController
@RequestMapping("/api/admin/chat")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
public class AdminChatController {
    private final ChatService chatService;

    @GetMapping("/conversations/open")
    public ResponseEntity<Page<ChatConversationResponse>> getOpenConversations(Pageable pageable) {
        log.info("GET /api/admin/chat/conversations/open");
        var response = chatService.getOpenConversations(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations/{id}")
    public ResponseEntity<ChatConversationResponse> getConversation(@PathVariable Long id) {
        log.info("GET /api/admin/chat/conversations/{}", id);
        var response = chatService.getConversationById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/conversations/{id}/assign")
    public ResponseEntity<ChatConversationResponse> assignConversation(
            @PathVariable Long id,
            @RequestParam Long agentId) {
        log.info("POST /api/admin/chat/conversations/{}/assign to agent {}", id, agentId);
        var response = chatService.assignConversation(id, agentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/conversations/{id}/reply")
    public ResponseEntity<ChatMessageResponse> sendReply(
            @PathVariable Long id,
            @Valid @RequestBody CreateChatMessageRequest request) {
        log.info("POST /api/admin/chat/conversations/{}/reply", id);
        
        var modifiedRequest = new CreateChatMessageRequest(
            id,
            request.message(),
            request.senderType() != null ? request.senderType() : "ADMIN"
        );
        
        var response = chatService.sendAdminReply(modifiedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<Page<ChatMessageResponse>> getMessages(@PathVariable Long id, Pageable pageable) {
        log.info("GET /api/admin/chat/conversations/{}/messages", id);
        var response = chatService.getConversationMessages(id, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/conversations/{id}/status")
    public ResponseEntity<ChatConversationResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("POST /api/admin/chat/conversations/{}/status - {}", id, status);
        var response = chatService.updateConversationStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/conversations/{id}/resolve")
    public ResponseEntity<ChatConversationResponse> resolveConversation(@PathVariable Long id) {
        log.info("POST /api/admin/chat/conversations/{}/resolve", id);
        var response = chatService.updateConversationStatus(id, "RESOLVED");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/conversations/{id}/close")
    public ResponseEntity<Void> closeConversation(@PathVariable Long id) {
        log.info("POST /api/admin/chat/conversations/{}/close", id);
        chatService.closeConversation(id);
        return ResponseEntity.ok().build();
    }
}
