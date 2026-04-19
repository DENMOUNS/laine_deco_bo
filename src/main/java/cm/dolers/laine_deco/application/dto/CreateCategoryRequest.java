package cm.dolers.laine_deco.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
    @NotBlank(message = "Le nom est obligatoire")
    String name,
    String description,
    String image
) {}
