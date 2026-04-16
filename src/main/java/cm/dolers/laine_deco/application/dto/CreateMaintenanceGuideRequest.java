package cm.dolers.laine_deco.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaintenanceGuideRequest {
    private String title;
    private String content;
    private String instructions;
    private Long categoryId;
    private String brand;
    private Long productId;
    private byte[] image;
    private Boolean isActive = true;
}
