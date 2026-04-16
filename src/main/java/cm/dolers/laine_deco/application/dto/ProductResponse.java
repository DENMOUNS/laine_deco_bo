package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;

/**
 * Record DTO pour les réponses de produit (Java 17+)
 * Inclut les données de produit avec marges calculées
 * 
 * Les Records remplacent automatiquement:
 * - Constructeurs
 * - Getters
 * - toString()
 * - equals()
 * - hashCode()
 */
public record ProductResponse(
    Long id,
    String sku,
    String name,
    String description,
    BigDecimal salePrice,
    BigDecimal costPrice,
    Integer stockQuantity,
    Integer reorderLevel,
    BigDecimal margin,
    BigDecimal marginPercentage
) {}
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getMarginPercentage() {
        return marginPercentage;
    }

    public void setMarginPercentage(BigDecimal marginPercentage) {
        this.marginPercentage = marginPercentage;
    }
}
