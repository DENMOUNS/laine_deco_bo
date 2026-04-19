package cm.dolers.laine_deco.infrastructure.i18n;

import lombok.RequiredArgsConstructor;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Service pour gérer les messages multilingues
 * Utilise MessageSource de Spring Boot
 */
@Service
@RequiredArgsConstructor

public class MessageService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory
            .getLogger(cm.dolers.laine_deco.infrastructure.i18n.MessageService.class);
    private final MessageSource messageSource;

    /**
     * Récupère un message traduit pour la locale actuelle
     */
    public String getMessage(String code) {
        return getMessage(code, new Object[] {});
    }

    /**
     * Récupère un message traduit avec des paramètres
     */
    public String getMessage(String code, Object... args) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            log.warn("Message not found for code: {}", code);
            return code;
        }
    }

    /**
     * Récupère un message traduit pour une locale spécifique
     */
    public String getMessage(String code, Locale locale) {
        return getMessage(code, new Object[] {}, locale);
    }

    /**
     * Récupère un message traduit pour une locale spécifique avec paramètres
     */
    public String getMessage(String code, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            log.warn("Message not found for code: {} in locale: {}", code, locale);
            return code;
        }
    }

    /**
     * Récupère la locale actuelle
     */
    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * Récupère le code langue actuel (ex: "fr", "en")
     */
    public String getCurrentLanguage() {
        return LocaleContextHolder.getLocale().getLanguage();
    }
}
