package cm.dolers.laine_deco.infrastructure.security;

import cm.dolers.laine_deco.domain.exception.AuthException;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilitaire centralisé pour récupérer l'utilisateur actuellement authentifié.
 * Lève une AuthException si l'utilisateur n'est pas authentifié (jamais de fallback silencieux).
 */
public final class SecurityUtils {

    private SecurityUtils() {
        // Classe utilitaire, pas d'instanciation
    }

    /**
     * Récupère l'ID de l'utilisateur connecté depuis le SecurityContext.
     *
     * @return l'ID de l'utilisateur connecté
     * @throws AuthException si l'utilisateur n'est pas authentifié
     */
    public static Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser user) {
            return user.getId();
        }
        throw new AuthException(ErrorCode.AUTH_UNAUTHORIZED, "Utilisateur non authentifié");
    }

    /**
     * Récupère l'utilisateur connecté depuis le SecurityContext.
     *
     * @return l'AuthenticatedUser connecté
     * @throws AuthException si l'utilisateur n'est pas authentifié
     */
    public static AuthenticatedUser getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser user) {
            return user;
        }
        throw new AuthException(ErrorCode.AUTH_UNAUTHORIZED, "Utilisateur non authentifié");
    }
}
