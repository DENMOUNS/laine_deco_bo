package cm.dolers.laine_deco.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFAQRequest {
    private String question;
    private String answer;
    private Integer displayOrder = 0;
    private Boolean isActive = true;
}
