package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.UserLoyaltyResponse;
import cm.dolers.laine_deco.application.dto.LoyaltyRedemptionResponse;
import cm.dolers.laine_deco.application.dto.RedeemRewardRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * Service pour gérer la loyauté et les points
 */
public interface LoyaltyService {
    /**
     * Ajoute des points à un utilisateur après une commande
     * Règles:
     * - 10 points par commande (base)
     * - 15 points si montant > 100.000
     */
    void addPointsFromOrder(Long userId, BigDecimal orderAmount);

    /**
     * Récupère le profil de loyauté d'un utilisateur
     */
    UserLoyaltyResponse getUserLoyalty(Long userId);

    /**
     * Redémer une récompense (échange des points)
     */
    LoyaltyRedemptionResponse redeemReward(Long userId, RedeemRewardRequest request);

    /**
     * Récupère l'historique des rédemptions
     */
    Page<LoyaltyRedemptionResponse> getUserRedemptions(Long userId, Pageable pageable);

    /**
     * Récupère les rédemptions en attente (non encore appliquées)
     */
    Page<LoyaltyRedemptionResponse> getPendingRedemptions(Long userId, Pageable pageable);
}
