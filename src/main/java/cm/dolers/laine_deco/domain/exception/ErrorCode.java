package cm.dolers.laine_deco.domain.exception;

public enum ErrorCode {
    CALC_NOT_FOUND("CALC_NOT_FOUND", "Calculation not found", 404),
    CONFIG_NOT_FOUND("CONFIG_NOT_FOUND", "Configuration not found", 404),
    PROMO_NOT_FOUND("PROMO_NOT_FOUND", "Promo event not found", 404),
    PAYMENT_INVALID_AMOUNT("PAYMENT_INVALID_AMOUNT", "Payment invalid amount", 400),
    PAYMENT_ALREADY_REFUNDED("PAYMENT_ALREADY_REFUNDED", "Payment already refunded", 409),
    PAYMENT_NOT_SUCCESS("PAYMENT_NOT_SUCCESS", "Payment not successful", 400),
    PAYMENT_NOT_FOUND("PAYMENT_NOT_FOUND", "Payment not found", 404),
    // ==================== AUTHENTIFICATION ====================
    AUTH_INVALID_CREDENTIALS("AUTH_001", "Identifiants invalides", 401),
    AUTH_USER_NOT_FOUND("AUTH_002", "Utilisateur non trouvé", 404),
    AUTH_USER_DISABLED("AUTH_003", "Utilisateur désactivé", 403),
    AUTH_TOKEN_EXPIRED("AUTH_004", "Token expiré", 401),
    AUTH_TOKEN_INVALID("AUTH_005", "Token invalide", 401),
    AUTH_UNAUTHORIZED("AUTH_006", "Non autorisé", 403),
    AUTH_PASSWORD_WEAK("AUTH_007", "Mot de passe trop faible", 400),
    AUTH_EMAIL_EXISTS("AUTH_008", "Cet email existe déjà", 409),

    // ==================== PRODUITS ====================
    CATEGORY_ALREADY_EXISTS("CAT_001", "Catégorie existe déjà", 409),
    CATEGORY_NOT_FOUND("CAT_002", "Catégorie non trouvée", 404),
    PRODUCT_NOT_FOUND("PROD_001", "Produit non trouvé", 404),
    PRODUCT_OUT_OF_STOCK("PROD_002", "Produit en rupture de stock", 400),
    PRODUCT_INVALID_PRICE("PROD_003", "Prix invalide", 400),
    PRODUCT_SKU_EXISTS("PROD_004", "Ce SKU existe déjà", 409),
    PRODUCT_CATEGORY_NOT_FOUND("PROD_005", "Catégorie non trouvée", 404),

    // ==================== COMMANDES ====================
    ORDER_NOT_FOUND("ORDER_001", "Commande non trouvée", 404),
    ORDER_INVALID_STATUS("ORDER_002", "Statut de commande invalide", 400),
    ORDER_CANNOT_CANCEL("ORDER_003", "Impossible d'annuler cette commande", 400),
    ORDER_EMPTY_CART("ORDER_004", "Le panier est vide", 400),
    ORDER_INVALID_TOTAL("ORDER_005", "Total de commande invalide", 400),

    // ==================== PAIEMENT ====================
    PAYMENT_FAILED("PAY_001", "Le paiement a échoué", 402),
    PAYMENT_METHOD_INVALID("PAY_002", "Méthode de paiement invalide", 400),
    PAYMENT_AMOUNT_ZERO("PAY_003", "Le montant doit être supérieur à zéro", 400),
    COUPON_EXPIRED("PAY_004", "Coupon expiré", 400),
    COUPON_USAGE_LIMIT_EXCEEDED("PAY_005", "Limite d'utilisation du coupon dépassée", 400),
    COUPON_NOT_FOUND("PAY_006", "Coupon non trouvé", 404),
    COUPON_CODE_DUPLICATE("PAY_007", "Code coupon dupliqué", 409),
    COUPON_INACTIVE("PAY_008", "Coupon inactif", 400),
    COUPON_LIMIT_EXCEEDED("PAY_009", "Limite dépassée", 400),

    // ==================== AVIS & NOTES ====================
    REVIEW_NOT_FOUND("REV_001", "Avis non trouvé", 404),
    REVIEW_INVALID_RATING("REV_002", "Note doit être entre 1 et 5", 400),
    REVIEW_ALREADY_EXISTS("REV_003", "Vous avez déjà noté ce produit", 409),
    REVIEW_UNAUTHORIZED("REV_004", "Seul le client peut modifier son avis", 403),

    // ==================== RMA (RETOURS) ====================
    RMA_NOT_FOUND("RMA_001", "Demande de retour non trouvée", 404),
    RMA_ORDER_NOT_ELIGIBLE("RMA_002", "La commande n'est pas éligible pour retour", 400),
    RMA_INVALID_REASON("RMA_003", "Raison de retour invalide", 400),
    RMA_DEADLINE_EXPIRED("RMA_004", "Délai de retour dépassé (30 jours)", 400),

    // ==================== TRICOT ====================
    PROJECT_NOT_FOUND("KNIT_001", "Projet non trouvé", 404),
    PROJECT_INVALID_TARGET("KNIT_002", "Objectif de rangées invalide", 400),
    PATTERN_NOT_FOUND("KNIT_003", "Modèle non trouvé", 404),

    // ==================== BLOG & CONTENU ====================
    POST_NOT_FOUND("BLOG_000", "Post non trouvé", 404),
    BLOG_POST_NOT_FOUND("BLOG_001", "Article non trouvé", 404),
    BLOG_POST_TITLE_EMPTY("BLOG_002", "Le titre de l'article est vide", 400),

    // ==================== FINANCES ====================
    INVOICE_NOT_FOUND("FIN_000", "Facture non trouvée", 404),
    EXPENSE_NOT_FOUND("FIN_001", "Dépense non trouvée", 404),
    EXPENSE_INVALID_AMOUNT("FIN_002", "Montant de dépense invalide", 400),
    EXPENSE_CATEGORY_NOT_FOUND("FIN_003", "Catégorie de dépense non trouvée", 404),

    // ==================== PROFIL UTILISATEUR ====================
    USER_NOT_FOUND("USER_001", "Utilisateur non trouvé", 404),
    USER_EMAIL_ALREADY_EXISTS("USER_002", "Cet email est déjà utilisé", 409),
    USER_PROFILE_UPDATE_FAILED("USER_003", "Échec de la mise à jour du profil", 400),
    USER_INSUFFICIENT_PERMISSIONS("USER_004", "Permissions insuffisantes", 403),

    // ==================== WISHLIST ====================
    WISHLIST_ITEM_NOT_FOUND("WISH_001", "Produit ne pas dans la wishlist", 404),
    WISHLIST_ITEM_ALREADY_EXISTS("WISH_002", "Produit déjà dans la wishlist", 409),

    // ==================== PANIER ABANDONNÉ ====================
    ABANDONED_CART_NOT_FOUND("CART_001", "Panier abandonné non trouvé", 404),

    // ==================== GESTION DES RÔLES ====================
    ROLE_NOT_FOUND("ROLE_001", "Rôle non trouvé", 404),
    ROLE_INVALID("ROLE_002", "Rôle invalide", 400),
    ROLE_CANNOT_DELETE("ROLE_003", "Impossible de supprimer ce rôle", 400),

    // ==================== NOTIFICATIONS ====================
    NOTIFICATION_NOT_FOUND("NOTIF_001", "Notification non trouvée", 404),

    // ==================== CHAT ====================
    CHAT_NOT_FOUND("CHAT_000", "Chat non trouvé", 404),
    CHAT_MSG_NOT_FOUND("CHAT_000", "Chat msg non trouvé", 404),
    CONVERSATION_NOT_FOUND("CHAT_001", "Conversation non trouvée", 404),
    MESSAGE_NOT_FOUND("CHAT_002", "Message non trouvé", 404),

    // ==================== ERREURS SERVEUR ====================
    INTERNAL_ERROR("SYS_001", "Erreur interne du serveur", 500),
    SERVICE_UNAVAILABLE("SYS_002", "Service indisponible", 503),
    DATABASE_ERROR("SYS_003", "Erreur de base de données", 500),
    OPERATION_FAILED("SYS_004", "Opération échouée", 500),
    INVALID_REQUEST("SYS_005", "Requête invalide", 400),
    RESOURCE_NOT_FOUND("SYS_006", "Ressource non trouvée", 404),
    CONFLICT("SYS_007", "Conflit", 409);

    private final String code;
    private final String message;
    private final int httpStatus;

    ErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}







