package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;

/**
 * Détail d'une ligne de commande
 */
public record OrderDetailResponse(
    Long id,
    Long productId,
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {}
