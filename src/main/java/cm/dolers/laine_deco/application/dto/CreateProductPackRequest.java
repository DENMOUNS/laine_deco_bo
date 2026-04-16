package cm.dolers.laine_deco.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductPackRequest {
    private String name;
    private String description;
    private Long price;
    private Long salePrice;
    private byte[] image;
    private List<PackProductItem> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackProductItem {
        private Long productId;
        private Integer quantity;
    }
}
