package cm.dolers.laine_deco.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBlogPostRequest(
    @NotBlank(message = "Le titre est obligatoire")
    String title,
    String excerpt,
    @NotBlank(message = "Le contenu est obligatoire")
    String content,
    String image,
    String author,
    String category,
    Boolean isActive
) {}

