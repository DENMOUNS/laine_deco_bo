package cm.dolers.laine_deco.domain.model;

/**
 * Types de badges que les clients peuvent gagner
 */
public enum BadgeType {
    FIRST_ORDER("Première Commande", "🎉", "Complétée lors de la première commande"),
    PREMIUM_REACHED("Premium Atteint", "⭐", "Une fois 500 points accumulés"),
    VIP_REACHED("VIP Atteint", "👑", "Une fois 1000 points accumulés"),
    GOLD_REACHED("Gold Atteint", "✨", "Une fois 3000 points accumulés"),
    LOYALTY_CHAMPION("Champion Fidélité", "🏆", "Plus de 50 commandes"),
    SUPER_SPENDER("Super Dépensier", "💰", "Total dépensé > 1.000.000"),
    EARLY_ADOPTER("Adopteur Précoce", "🚀", "Client depuis plus d'1 an");

    private final String label;
    private final String icon;
    private final String description;

    BadgeType(String label, String icon, String description) {
        this.label = label;
        this.icon = icon;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }
}
