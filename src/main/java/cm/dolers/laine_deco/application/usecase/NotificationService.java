package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interface Service pour Notifications
 * Gestion des notifications: messages, changements statut, système
 */
public interface NotificationService {
    // ==================== CRUD ====================
    
    /**
     * Créer une notification générique
     */
    NotificationResponse createNotification(CreateNotificationRequest request);
    
    /**
     * Récupérer une notification
     */
    NotificationResponse getNotificationById(Long notificationId);
    
    /**
     * Récupérer les notifications d'un utilisateur
     */
    Page<NotificationResponse> getUserNotifications(Long userId, Pageable pageable);
    
    /**
     * Récupérer les notifications non lues
     */
    List<NotificationResponse> getUnreadNotifications(Long userId);
    
    /**
     * Compter les notifications non lues
     */
    Long getUnreadNotificationCount(Long userId);
    
    /**
     * Marquer une notification comme lue
     */
    void markAsRead(Long notificationId);
    
    /**
     * Marquer toutes les notifications comme lues
     */
    void markAllAsRead(Long userId);
    
    /**
     * Supprimer une notification
     */
    void deleteNotification(Long notificationId);
    
    // ==================== Notifications spécifiques ====================
    
    /**
     * Notifier une réponse au chat
     */
    void notifyOnChatReply(ChatReplyNotificationRequest request);
    
    /**
     * Notifier un changement de statut de commande
     */
    void notifyOrderStatusChange(OrderStatusChangeNotificationRequest request);
    
    /**
     * Envoyer une annonce système
     */
    void broadcastSystemNotification(CreateSystemNotificationRequest request);
    
    /**
     * Notifier un badge obtenu
     */
    void notifyBadgeEarned(Long userId, String badgeName, String badgeDescription);
    
    /**
     * Notifier un coupon disponible
     */
    void notifyCouponAvailable(Long userId, String couponCode, String discount);
    
    /**
     * Notifier un événement promo actif
     */
    void notifyPromoEventActivated(String eventName, String description);
    
    /**
     * Notifier un RMA approuvé
     */
    void notifyRmaApproved(Long userId, String rmaNumber);
    
    /**
     * Notifier un RMA rejeté
     */
    void notifyRmaRejected(Long userId, String rmaNumber, String reason);
    
    /**
     * Récupérer notifications par type
     */
    Page<NotificationResponse> getNotificationsByType(Long userId, String type, Pageable pageable);
}

    void deleteNotification(Long notificationId);
    
    /**
     * Nettoyer les vieilles notifications
     */
    void cleanOldNotifications(int daysOld);
}
