package cm.dolers.laine_deco.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPackItemDto {
    private Long productId;
    private String productName;
    private Long productPrice;
    private Integer quantity;
    private byte[] productImage;
    private String productCategory;
    private String productBrand;
}
