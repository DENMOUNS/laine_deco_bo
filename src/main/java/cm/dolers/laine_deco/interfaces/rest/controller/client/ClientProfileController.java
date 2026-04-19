package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.UserService;
import cm.dolers.laine_deco.application.usecase.ClientProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Client Controller pour gérer le profil personnel
 * Endpoints: Mon profil, ma photo, mes coordonnées
 */
@RestController
@RequestMapping("/api/client/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientProfileController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientProfileController.class);
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
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof cm.dolers.laine_deco.infrastructure.security.AuthenticatedUser user) {
            return user.getId();
        }
        return 1L; // Fallback local
    }
}

