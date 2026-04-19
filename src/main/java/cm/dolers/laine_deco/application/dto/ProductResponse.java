package cm.dolers.laine_deco.application.dto;
import java.math.BigDecimal;
public record ProductResponse(Long id, String sku, String name, String description, BigDecimal salePrice, BigDecimal costPrice, Integer stockQuantity, Integer reorderLevel, BigDecimal margin, BigDecimal marginPercentage) {}
