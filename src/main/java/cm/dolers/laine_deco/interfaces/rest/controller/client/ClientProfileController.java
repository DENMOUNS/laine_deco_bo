package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.UserService;
import cm.dolers.laine_deco.application.usecase.ClientProfileService;
import cm.dolers.laine_deco.infrastructure.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientProfileController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientProfileController.class);
    private final UserService userService;
    private final ClientProfileService clientProfileService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("GET /api/client/profile/me - User: {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(@Valid @RequestBody CreateUserRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("PUT /api/client/profile/me - User: {}", userId);
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @GetMapping("/client")
    public ResponseEntity<ClientProfileResponse> getClientProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("GET /api/client/profile/client - User: {}", userId);
        return ResponseEntity.ok(clientProfileService.getClientProfile(userId));
    }

    @PutMapping("/client")
    public ResponseEntity<ClientProfileResponse> updateClientProfile(@Valid @RequestBody CreateClientProfileRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("PUT /api/client/profile/client - User: {}", userId);
        return ResponseEntity.ok(clientProfileService.createOrUpdateProfile(userId, request));
    }
}
