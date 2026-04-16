package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.UserBadgeResponse;
import cm.dolers.laine_deco.domain.model.BadgeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service pour gérer les badges utilisateur
 */
public interface BadgeService {
    /**
     * Ajoute un badge à un utilisateur
     */
    UserBadgeResponse awardBadge(Long userId, BadgeType badgeType, String description);

    /**
     * Récupère tous les badges d'un utilisateur
     */
    List<UserBadgeResponse> getUserBadges(Long userId);

    /**
     * Récupère les badges avec pagination
     */
    Page<UserBadgeResponse> getUserBadgesPaginated(Long userId, Pageable pageable);

    /**
     * Vérifie si un utilisateur a un badge
     */
    boolean hasBadge(Long userId, BadgeType badgeType);

    /**
     * Attribue automatiquement les badges selon les étapes atteintes
     * - Première commande
     * - Tier Premium atteint
     * - Tier VIP atteint
     * - Tier Gold atteint
     */
    void checkAndAwardAutomatic(Long userId);
}
