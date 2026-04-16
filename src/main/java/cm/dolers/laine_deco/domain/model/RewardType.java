package cm.dolers.laine_deco.domain.model;

import java.math.BigDecimal;

/**
 * Types de récompenses que les clients peuvent échanger avec leurs points
 * 
 * Échange de points:
 * - 500 points → Livraison gratuite
 * - 1000 points → Coupon 10.000
 * - 3000 points → Livraison gratuite + 10% réduction
 */
public enum RewardType {
    FREE_SHIPPING("Livraison Gratuite", 500, BigDecimal.valueOf(0), "Livraison gratuite sur la prochaine commande"),
    COUPON_10K("Coupon 10.000", 1000, BigDecimal.valueOf(10000), "Coupon de réduction 10.000 FCFA"),
    PREMIUM_REWARD("Livraison + 10% Réduction", 3000, BigDecimal.valueOf(10000), "Livraison gratuite + 10% réduction sur commande");

    private final String label;
    private final int pointsRequired;
    private final BigDecimal value;
    private final String description;

    RewardType(String label, int pointsRequired, BigDecimal value, String description) {
        this.label = label;
        this.pointsRequired = pointsRequired;
        this.value = value;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
