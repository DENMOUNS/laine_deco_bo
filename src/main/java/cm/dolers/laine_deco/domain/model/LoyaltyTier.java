package cm.dolers.laine_deco.domain.model;

/**
 * Niveaux de fidélité (tiers) basés sur les points accumulés
 * 
 * - STANDARD: 0-499 points (tout client par défaut)
 * - PREMIUM: 500-999 points (accès prioritaire)
 * - VIP: 1000-2999 points (privileges spéciaux)
 * - GOLD: 3000+ points (benefits maximum)
 */
public enum LoyaltyTier {
    STANDARD("Standard", 0, 499, "Client standard"),
    PREMIUM("Premium", 500, 999, "Client premium avec priorité"),
    VIP("VIP", 1000, 2999, "Client VIP avec avantages spéciaux"),
    GOLD("Gold", 3000, Integer.MAX_VALUE, "Client Gold avec bénéfices maximums");

    private final String label;
    private final int minPoints;
    private final int maxPoints;
    private final String description;

    LoyaltyTier(String label, int minPoints, int maxPoints, String description) {
        this.label = label;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Détermine le tier en fonction du nombre de points
     */
    public static LoyaltyTier fromPoints(int points) {
        if (points >= GOLD.minPoints) return GOLD;
        if (points >= VIP.minPoints) return VIP;
        if (points >= PREMIUM.minPoints) return PREMIUM;
        return STANDARD;
    }
}
