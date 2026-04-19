package cm.dolers.laine_deco.application.dto;
import java.math.BigDecimal;
import java.util.List;
public record ProductDetailResponse(Long id, String sku, String name, String description, BigDecimal salePrice, BigDecimal costPrice, BigDecimal margin, BigDecimal marginPercentage, Integer stockQuantity, Integer reorderLevel, String material, List<String> colors, String brand, BigDecimal rating, Boolean isNew, Boolean isSale, Boolean isAvailable, String warranty, Boolean isElectronic, Integer viewsCount, Integer salesCount, Long categoryId, java.time.Instant createdAt, java.time.Instant updatedAt) {}
