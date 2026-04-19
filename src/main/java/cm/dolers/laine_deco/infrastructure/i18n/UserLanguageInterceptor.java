package cm.dolers.laine_deco.infrastructure.i18n;

import cm.dolers.laine_deco.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Locale;

/**
 * Intercepteur pour appliquer automatiquement la langue préférée de
 * l'utilisateur
 * Priorité:
 * 1. Paramètre ?lang=en (explicit)
 * 2. Accept-Language header
 * 3. Langue préférée de l'utilisateur connecté
 * 4. Défaut: Français (fr)
 */
@Component
@RequiredArgsConstructor

public class UserLanguageInterceptor implements HandlerInterceptor {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserLanguageInterceptor.class);
    private final UserJpaRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("UserLanguageInterceptor - Processing request: {} {}", request.getMethod(), request.getRequestURI());

        // 1. Vérifier si le paramètre ?lang est présent (priorité haute)
        String langParam = request.getParameter("lang");
        if (langParam != null && (langParam.equals("fr") || langParam.equals("en"))) {
            log.debug("Language set from parameter: {}", langParam);
            setLocale(request, langParam);
            return true;
        }

        // 2. Si l'utilisateur est authentifié, récupérer sa langue préférée
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userEmail = authentication.getName();
            try {
                var userOpt = userRepository.findByEmail(userEmail);
                if (userOpt.isPresent()) {
                    String preferredLanguage = userOpt.get().getPreferredLanguage();
                    if (preferredLanguage != null) {
                        log.debug("Language set from user preference for {}: {}", userEmail, preferredLanguage);
                        setLocale(request, preferredLanguage);
                        return true;
                    }
                }
            } catch (Exception e) {
                log.warn("Error fetching user language preference: {}", e.getMessage());
            }
        }

        // 3. Accept-Language header est géré automatiquement par Spring
        // (AcceptHeaderLocaleResolver)
        log.debug("Using default language resolution (Accept-Language header or default FR)");
        return true;
    }

    /**
     * Configure la locale pour la requête
     */
    private void setLocale(HttpServletRequest request, String language) {
        try {
            Locale locale = Locale.forLanguageTag(language);
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
        } catch (Exception e) {
            log.warn("Error setting locale: {}", e.getMessage());
        }
    }
}
