package cm.dolers.laine_deco.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegalPageResponse {
    private Long id;
    private String type;
    private String title;
    private String content;
    private String summary;
    private Boolean isPublished;
    private String version;
    private String createdAt;
    private String updatedAt;
    private String publishedAt;
}
