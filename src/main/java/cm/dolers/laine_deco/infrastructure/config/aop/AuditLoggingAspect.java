package cm.dolers.laine_deco.infrastructure.config.aop;

import cm.dolers.laine_deco.application.usecase.AuditService;
import cm.dolers.laine_deco.infrastructure.config.RequestInfoExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * Aspect AOP pour capturer automatiquement les actions importantes
 * et les enregistrer dans les logs d'audit
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLoggingAspect {
    private final AuditService auditService;
    private static final ThreadLocal<Long> START_TIME = ThreadLocal.withInitial(System::currentTimeMillis);

    /**
     * Enregistre les opérations POST (CREATE)
     */
    @AfterReturning("@annotation(cm.dolers.laine_deco.infrastructure.config.aop.Auditable) && @within(org.springframework.web.bind.annotation.RestController)")
    public void logCreateOperation(JoinPoint joinPoint) {
        logAuditAction(joinPoint, "CREATE", "SUCCESS", null);
    }

    /**
     * Enregistre les opérations PUT (UPDATE)
     */
    @AfterReturning("@annotation(cm.dolers.laine_deco.infrastructure.config.aop.Auditable)")
    public void logUpdateOperation(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        if (methodName.contains("update") || methodName.contains("put")) {
            logAuditAction(joinPoint, "UPDATE", "SUCCESS", null);
        }
    }

    /**
     * Enregistre les opérations DELETE
     */
    @AfterReturning("@annotation(cm.dolers.laine_deco.infrastructure.config.aop.Auditable)")
    public void logDeleteOperation(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        if (methodName.contains("delete") || methodName.contains("remove")) {
            logAuditAction(joinPoint, "DELETE", "SUCCESS", null);
        }
    }

    /**
     * Enregistre les erreurs
     */
    @AfterThrowing(value = "@annotation(cm.dolers.laine_deco.infrastructure.config.aop.Auditable)", throwing = "exception")
    public void logFailedOperation(JoinPoint joinPoint, Exception exception) {
        logAuditAction(joinPoint, "ERROR", "FAILURE", exception.getMessage());
    }

    @Before("@annotation(cm.dolers.laine_deco.infrastructure.config.aop.Auditable)")
    public void startTimer(JoinPoint joinPoint) {
        START_TIME.set(System.currentTimeMillis());
    }

    /**
     * Méthode interne pour enregistrer une action
     */
    private void logAuditAction(JoinPoint joinPoint, String action, String status, String errorMessage) {
        try {
            long duration = System.currentTimeMillis() - START_TIME.get();
            START_TIME.remove();

            // Récupérer l'utilisateur courant
            Long userId = getCurrentUserId();
            String userEmail = getCurrentUserEmail();

            // Récupérer les informations du client
            HttpServletRequest request = getHttpRequest();
            String ipAddress = RequestInfoExtractor.getClientIpAddress(request);
            String userAgent = RequestInfoExtractor.getUserAgent(request);

            // Extraire les informations de la méthode
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            String description = String.format("%s.%s executed in %dms", className, methodName, duration);

            // Logger dans AuditService
            auditService.logAction(
                userId,
                userEmail != null ? userEmail : "ANONYMOUS",
                action,
                className,
                null, // entityId - pourrait être extrait des paramètres
                description,
                ipAddress,
                userAgent
            );

            log.debug("Audit logged: {} - {} - {}", action, className, methodName);
        } catch (Exception e) {
            log.error("Error logging audit action", e);
            // Ne pas lever d'exception pour ne pas impacter la logique métier
        }
    }

    /**
     * Extrait l'ID de l'utilisateur du contexte de sécurité
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                // Utiliser le username, pourrait être parsé comme ID
                try {
                    String username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
                    return Long.parseLong(username);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Extrait l'email de l'utilisateur du contexte de sécurité
     */
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            }
            return authentication.getName();
        }
        return null;
    }

    /**
     * Extrait la HttpServletRequest du contexte
     */
    private HttpServletRequest getHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }
}
