package cm.dolers.laine_deco.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FAQResponse {
    private Long id;
    private String question;
    private String answer;
    private Integer displayOrder;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}
