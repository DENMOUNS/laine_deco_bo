package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

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
}

/**
 * Interface Service pour Projets Tricot
 */
public interface KnittingProjectService {
    KnittingProjectResponse createProject(CreateKnittingProjectRequest request);
    KnittingProjectResponse getProjectById(Long projectId);
    Page<KnittingProjectResponse> getUserProjects(Long userId, Pageable pageable);
    KnittingProjectResponse updateProject(Long projectId, CreateKnittingProjectRequest request);
    void deleteProject(Long projectId);
    void updateProjectProgress(Long projectId, Integer progress);
    void completeProject(Long projectId);
    List<KnittingProjectResponse> getUserActiveProjects(Long userId);
}

/**
 * Interface Service pour Paiements
 */
public interface PaymentService {
    PaymentResponse processPayment(CreatePaymentRequest request);
    PaymentResponse getPaymentById(Long paymentId);
    PaymentResponse getPaymentByTransactionId(String transactionId);
    Page<PaymentResponse> getOrderPayments(Long orderId);
    void refundPayment(Long paymentId);
    void validatePayment(Long paymentId);
}
