package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

import cm.dolers.laine_deco.application.dto.AuthResult;
import cm.dolers.laine_deco.application.usecase.AuthApplicationService;
import cm.dolers.laine_deco.infrastructure.security.AuthenticatedUser;
import cm.dolers.laine_deco.interfaces.rest.request.ForgotPasswordRequest;
import cm.dolers.laine_deco.interfaces.rest.request.GoogleSigninRequest;
import cm.dolers.laine_deco.interfaces.rest.request.LoginRequest;
import cm.dolers.laine_deco.interfaces.rest.request.ResetPasswordRequest;
import cm.dolers.laine_deco.interfaces.rest.request.SignupRequest;
import cm.dolers.laine_deco.interfaces.rest.response.AuthResponse;
import cm.dolers.laine_deco.interfaces.rest.response.MessageResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class PublicAuthController {
    private final AuthApplicationService authService;

    public PublicAuthController(AuthApplicationService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        AuthResult result = authService.signup(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getConfirmPassword(),
                false);
        return ResponseEntity.ok(toResponse(result));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = authService.login(request.getEmail(), request.getPassword(), request.isRememberMe());
        return ResponseEntity.ok(toResponse(result));
    }

    @PostMapping("/google-signin")
    public ResponseEntity<AuthResponse> googleSignin(@Valid @RequestBody GoogleSigninRequest request) {
        AuthResult result = authService.googleSignin(request.getIdToken(), request.isRememberMe());
        return ResponseEntity.ok(toResponse(result));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        String token = extractToken(authHeader);
        if (token != null) {
            authService.logout(token);
        }
        return ResponseEntity.ok(new MessageResponse("Logged out"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String token = authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(new MessageResponse(
                "If this email exists, password reset instructions were sent.",
                token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword(), request.getConfirmPassword());
        return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<MessageResponse> me(Authentication authentication, Principal principal) {
        if (authentication == null || principal == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Unauthorized"));
        }
        if (authentication.getPrincipal() instanceof AuthenticatedUser user) {
            return ResponseEntity.ok(new MessageResponse("Connected as " + user.getEmail()));
        }
        return ResponseEntity.ok(new MessageResponse("Connected"));
    }

    private AuthResponse toResponse(AuthResult result) {
        return new AuthResponse(
                result.getToken(),
                result.getExpiresAtEpochSeconds(),
                result.getEmail(),
                result.getName());
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}


