package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.infrastructure.i18n.MessageService;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserJpaRepository;
import cm.dolers.laine_deco.domain.model.ThemePreference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Endpoint pour gérer les préférences utilisateur (langue, etc.)
 */
@RestController
@RequestMapping("/api/user/preferences")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
public class UserPreferencesController {
    private final UserJpaRepository userRepository;
    private final MessageService messageService;

    /**
     * GET /api/user/preferences/language
     * Récupère la langue préférée de l'utilisateur connecté
     */
    @GetMapping("/language")
    public ResponseEntity<Map<String, String>> getPreferredLanguage() {
        log.info("GET /api/user/preferences/language");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = auth.getName();
        var user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String language = user.get().getPreferredLanguage() != null ? 
            user.get().getPreferredLanguage() : "fr";

        return ResponseEntity.ok(Map.of(
            "preferredLanguage", language,
            "supportedLanguages", "fr,en"
        ));
    }

    /**
     * POST /api/user/preferences/language?lang=en
     * Met à jour la langue préférée de l'utilisateur connecté
     */
    @PostMapping("/language")
    public ResponseEntity<Map<String, String>> setPreferredLanguage(
        @RequestParam String lang
    ) {
        log.info("POST /api/user/preferences/language - Setting language to: {}", lang);

        // Validation de la langue
        if (!lang.matches("^(fr|en)$")) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", messageService.getMessage("error.bad.request"),
                "message", "Language must be 'fr' or 'en'"
            ));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = auth.getName();
        var user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Mettre à jour la langue
        user.get().setPreferredLanguage(lang);
        userRepository.save(user.get());

        log.info("User {} language preference updated to: {}", email, lang);

        return ResponseEntity.ok(Map.of(
            "message", messageService.getMessage("success.item.updated"),
            "preferredLanguage", lang,
            "currentLanguage", messageService.getCurrentLanguage()
        ));
    }

    /**
     * GET /api/user/preferences
     * Récupère toutes les préférences utilisateur
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> getUserPreferences() {
        log.info("GET /api/user/preferences");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = auth.getName();
        var user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Map<String, String> preferences = new HashMap<>();
        preferences.put("preferredLanguage", user.get().getPreferredLanguage() != null ? 
            user.get().getPreferredLanguage() : "fr");
        preferences.put("preferredTheme", user.get().getPreferredTheme() != null ? 
            user.get().getPreferredTheme().getValue() : "light");
        preferences.put("email", user.get().getEmail());
        preferences.put("name", user.get().getName());

        return ResponseEntity.ok(preferences);
    }

    /**
     * GET /api/user/preferences/theme
     * Récupère la préférence de thème de l'utilisateur connecté
     */
    @GetMapping("/theme")
    public ResponseEntity<Map<String, String>> getPreferredTheme() {
        log.info("GET /api/user/preferences/theme");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = auth.getName();
        var user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String theme = user.get().getPreferredTheme() != null ? 
            user.get().getPreferredTheme().getValue() : "light";

        return ResponseEntity.ok(Map.of(
            "preferredTheme", theme,
            "supportedThemes", "light,dark"
        ));
    }

    /**
     * POST /api/user/preferences/theme?theme=dark
     * Met à jour le thème préféré de l'utilisateur connecté
     */
    @PostMapping("/theme")
    public ResponseEntity<Map<String, String>> setPreferredTheme(
        @RequestParam String theme
    ) {
        log.info("POST /api/user/preferences/theme - Setting theme to: {}", theme);

        // Validation du thème
        ThemePreference themePreference = ThemePreference.fromString(theme);
        if (themePreference == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", messageService.getMessage("error.bad.request"),
                "message", "Theme must be 'light' or 'dark'"
            ));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = auth.getName();
        var user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Mettre à jour le thème
        user.get().setPreferredTheme(themePreference);
        userRepository.save(user.get());

        log.info("User {} theme preference updated to: {}", email, theme);

        return ResponseEntity.ok(Map.of(
            "message", messageService.getMessage("success.item.updated"),
            "preferredTheme", themePreference.getValue()
        ));
    }
}
