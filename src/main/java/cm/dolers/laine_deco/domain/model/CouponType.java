package cm.dolers.laine_deco.domain.model;

/**
 * Types de coupons/codes promo
 * 
 * ALL_PRODUCTS: Remise en pourcentage sur tous les produits du panier
 * SINGLE_PRODUCT: Remise en pourcentage sur un seul produit
 * FREE_SHIPPING: Offre la livraison gratuite (montant fixe)
 * FIXED_AMOUNT: Déduit un montant fixe de la commande
 */
public enum CouponType {
    ALL_PRODUCTS("Remise sur tous les produits"),
    SINGLE_PRODUCT("Remise sur un produit spécifique"),
    FREE_SHIPPING("Livraison gratuite"),
    FIXED_AMOUNT("Montant fixe de réduction");

    private final String label;

    CouponType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
