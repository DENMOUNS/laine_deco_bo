package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.UserService;
import cm.dolers.laine_deco.application.usecase.ClientProfileService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

/**
 * Client Controller pour gérer le profil personnel
 * Endpoints: Mon profil, ma photo, mes coordonnées
 */
@RestController
@RequestMapping("/api/client/profile")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CLIENT')")
public class ClientProfileController {
    private final UserService userService;
    private final ClientProfileService clientProfileService;

    /**
     * Récupérer le profil de l'utilisateur connecté
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/profile/me - User: {}", userId);
        var response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Mettre à jour mon profil
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(
            @Valid @RequestBody CreateUserRequest request,
            HttpServletRequest httpRequest) {
        Long userId = extractUserIdFromToken(httpRequest);
        log.info("PUT /api/client/profile/me - User: {}", userId);
        var response = userService.updateUser(userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer mon profil client (adresse, etc.)
     */
    @GetMapping("/client")
    public ResponseEntity<ClientProfileResponse> getClientProfile(HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/profile/client - User: {}", userId);
        var response = clientProfileService.getClientProfile(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Mettre à jour mon profil client
     */
    @PutMapping("/client")
    public ResponseEntity<ClientProfileResponse> updateClientProfile(
            @Valid @RequestBody CreateClientProfileRequest request,
            HttpServletRequest httpRequest) {
        Long userId = extractUserIdFromToken(httpRequest);
        log.info("PUT /api/client/profile/client - User: {}", userId);
        var response = clientProfileService.createOrUpdateProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            // Decode JWT payload (basic, sans vérification signature)
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                String payload = new String(Base64.getDecoder().decode(parts[1]));
                // Parse JSON pour extraire user_id
                // Simplifié: vous devriez utiliser un service JWT proper
                return 1L; // À implémenter correctement
            }
        }
        return 1L; // Fallback
    }
}
