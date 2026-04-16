package cm.dolers.laine_deco.infrastructure.config.aop;

import java.lang.annotation.*;

/**
 * Annotation pour marquer les méthodes qui doivent être auditées
 * Utilisée en conjonction avec l'aspect AOP AuditLoggingAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    /**
     * Action à enregistrer (CREATE, UPDATE, DELETE, etc.)
     * Si non spécifié, sera déterminé par le nom de la méthode
     */
    String action() default "";

    /**
     * Type d'entité affectée
     * Si non spécifié, sera utilisé le nom du contrôleur/service
     */
    String entityType() default "";

    /**
     * Description personnalisée (optionnel)
     */
    String description() default "";
}
