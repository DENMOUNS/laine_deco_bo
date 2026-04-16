package cm.dolers.laine_deco.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceGuideResponse {
    private Long id;
    private String title;
    private String content;
    private String instructions;
    private String scope;
    private Long categoryId;
    private String categoryName;
    private String brand;
    private Long productId;
    private String productName;
    private byte[] image;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}
