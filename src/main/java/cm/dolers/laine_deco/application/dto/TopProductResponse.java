package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;

/**
 * DTO pour les top produits
 */
public record TopProductResponse(
        Long productId,
        String productName,
        String sku,
        Integer salesCount,
        BigDecimal revenue,
        Double rating) {
}
