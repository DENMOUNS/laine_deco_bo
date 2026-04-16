package cm.dolers.laine_deco.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPackResponse {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Long salePrice;
    private Long discountedPrice;
    private Integer promotionalDiscount;
    private byte[] image;
    private List<ProductPackItemDto> items;
    private Integer totalProductCount;
    private Integer uniqueProductCount;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}
