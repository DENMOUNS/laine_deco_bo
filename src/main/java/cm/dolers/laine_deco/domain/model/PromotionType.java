package cm.dolers.laine_deco.domain.model;

/**
 * Types de promotions disponibles
 */
public enum PromotionType {
    FLASH_SALE("Flash Sale", "Vente flash temporaire"),
    CATEGORY_DISCOUNT("Category Discount", "Réduction sur une catégorie"),
    BRAND_DISCOUNT("Brand Discount", "Réduction sur une marque"),
    SEASONAL_SALE("Seasonal Sale", "Solde saisonnière"),
    PERCENTAGE_OFF("Percentage Off", "Réduction en pourcentage");

    private final String value;
    private final String label;

    PromotionType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
