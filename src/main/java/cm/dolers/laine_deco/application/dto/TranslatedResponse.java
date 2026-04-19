package cm.dolers.laine_deco.application.dto;

import java.util.Map;

/**
 * DTO pour réponse API multilingue
 * Wrapper pour inclure les traductions dans les réponses
 */
public record TranslatedResponse<T>(
    T data,
    Map<String, String> labels,  // Labels traduits
    String language,              // Langue actuelle (fr, en)
    Long timestamp
) {}

