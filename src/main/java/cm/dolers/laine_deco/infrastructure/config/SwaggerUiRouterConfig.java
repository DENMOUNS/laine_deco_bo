package cm.dolers.laine_deco.infrastructure.config;

/**
 * SUPPRESSION de l'ancien SwaggerUiController manuel.
 *
 * PROBLÈME : L'ancien code avait un @RestController qui captait /swagger-ui/**
 * et /swagger-ui.html, créant un conflit avec SpringDoc qui gère ces routes nativement.
 *
 * SOLUTION : SpringDoc 2.x (compatible Spring Boot 3) gère automatiquement :
 *   - /swagger-ui/index.html  -> Interface Swagger UI
 *   - /swagger-ui.html        -> Redirect vers /swagger-ui/index.html
 *   - /v3/api-docs            -> JSON OpenAPI
 *   - /v3/api-docs.yaml       -> YAML OpenAPI
 *
 * Cette classe est intentionnellement vide. Ne pas y remettre de controller.
 */
public class SwaggerUiRouterConfig {
    // Classe vide - SpringDoc gère tout automatiquement
}