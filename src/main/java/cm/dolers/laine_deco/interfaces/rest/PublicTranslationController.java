package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.infrastructure.i18n.MessageService;
import cm.dolers.laine_deco.application.mapper.TranslationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

/**
 * Endpoint public pour la gestion des traductions et langues
 */
@RestController
@RequestMapping("/api/public/translations")
@RequiredArgsConstructor
@Slf4j
public class PublicTranslationController {
    private final MessageService messageService;
    private final TranslationMapper translationMapper;

    /**
     * GET /api/public/translations/current-language
     * Récupère la langue actuelle du client
     */
    @GetMapping("/current-language")
    public ResponseEntity<Map<String, String>> getCurrentLanguage() {
        log.info("GET /api/public/translations/current-language");
        String currentLang = messageService.getCurrentLanguage();
        Locale locale = messageService.getCurrentLocale();
        
        return ResponseEntity.ok(Map.of(
            "language", currentLang,
            "locale", locale.toString(),
            "displayName", locale.getDisplayLanguage(Locale.ENGLISH)
        ));
    }

    /**
     * GET /api/public/translations/supported-languages
     * Liste les langues supportées
     */
    @GetMapping("/supported-languages")
    public ResponseEntity<Map<String, String>> getSupportedLanguages() {
        log.info("GET /api/public/translations/supported-languages");
        Map<String, String> languages = new HashMap<>();
        languages.put("fr", "Français");
        languages.put("en", "English");
        return ResponseEntity.ok(languages);
    }

    /**
     * GET /api/public/translations/product-labels
     * Récupère les labels traduits pour les produits
     */
    @GetMapping("/product-labels")
    public ResponseEntity<Map<String, String>> getProductLabels() {
        log.info("GET /api/public/translations/product-labels");
        return ResponseEntity.ok(translationMapper.getProductLabels());
    }

    /**
     * GET /api/public/translations/category-labels
     * Récupère les labels traduits pour les catégories
     */
    @GetMapping("/category-labels")
    public ResponseEntity<Map<String, String>> getCategoryLabels() {
        log.info("GET /api/public/translations/category-labels");
        return ResponseEntity.ok(translationMapper.getCategoryLabels());
    }

    /**
     * GET /api/public/translations/order-labels
     * Récupère les labels traduits pour les commandes
     */
    @GetMapping("/order-labels")
    public ResponseEntity<Map<String, String>> getOrderLabels() {
        log.info("GET /api/public/translations/order-labels");
        return ResponseEntity.ok(translationMapper.getOrderLabels());
    }

    /**
     * GET /api/public/translations/error-messages
     * Récupère les messages d'erreur traduits
     */
    @GetMapping("/error-messages")
    public ResponseEntity<Map<String, String>> getErrorMessages() {
        log.info("GET /api/public/translations/error-messages");
        return ResponseEntity.ok(translationMapper.getErrorMessages());
    }

    /**
     * GET /api/public/translations/success-messages
     * Récupère les messages de succès traduits
     */
    @GetMapping("/success-messages")
    public ResponseEntity<Map<String, String>> getSuccessMessages() {
        log.info("GET /api/public/translations/success-messages");
        return ResponseEntity.ok(translationMapper.getSuccessMessages());
    }

    /**
     * GET /api/public/translations/message?code=product.name&lang=en
     * Récupère une traduction spécifique
     */
    @GetMapping("/message")
    public ResponseEntity<Map<String, String>> getMessage(
        @RequestParam String code,
        @RequestParam(required = false) String lang
    ) {
        log.info("GET /api/public/translations/message - code: {}, lang: {}", code, lang);
        
        String message;
        if (lang != null) {
            Locale locale = Locale.forLanguageTag(lang);
            message = messageService.getMessage(code, locale);
        } else {
            message = messageService.getMessage(code);
        }

        return ResponseEntity.ok(Map.of(
            "code", code,
            "message", message,
            "language", lang != null ? lang : messageService.getCurrentLanguage()
        ));
    }

    /**
     * GET /api/public/translations/all
     * Récupère tous les labels et messages (complet)
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllTranslations() {
        log.info("GET /api/public/translations/all");
        Map<String, Object> allTranslations = new HashMap<>();
        
        allTranslations.put("currentLanguage", messageService.getCurrentLanguage());
        allTranslations.put("productLabels", translationMapper.getProductLabels());
        allTranslations.put("categoryLabels", translationMapper.getCategoryLabels());
        allTranslations.put("orderLabels", translationMapper.getOrderLabels());
        allTranslations.put("errorMessages", translationMapper.getErrorMessages());
        allTranslations.put("successMessages", translationMapper.getSuccessMessages());
        
        return ResponseEntity.ok(allTranslations);
    }
}
