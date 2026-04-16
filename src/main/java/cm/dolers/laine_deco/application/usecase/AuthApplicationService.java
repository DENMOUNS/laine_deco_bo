package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.AuthResult;
import cm.dolers.laine_deco.domain.exception.AuthException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.AuthProvider;
import cm.dolers.laine_deco.infrastructure.persistence.entity.AuthSessionEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PasswordResetTokenEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.AuthSessionJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.PasswordResetTokenJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserJpaRepository;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
public class AuthApplicationService {
    private final UserJpaRepository userRepository;
    private final AuthSessionJpaRepository sessionRepository;
    private final PasswordResetTokenJpaRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final RestClient restClient;
    private final long accessTokenMinutes;
    private final long rememberMeDays;
    private final long forgotPasswordMinutes;

    public AuthApplicationService(
            UserJpaRepository userRepository,
            AuthSessionJpaRepository sessionRepository,
            PasswordResetTokenJpaRepository resetTokenRepository,
            PasswordEncoder passwordEncoder,
            JavaMailSender mailSender,
            @Value("${app.auth.access-token-minutes:60}") long accessTokenMinutes,
            @Value("${app.auth.remember-me-days:30}") long rememberMeDays,
            @Value("${app.auth.forgot-password-minutes:20}") long forgotPasswordMinutes) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.accessTokenMinutes = accessTokenMinutes;
        this.rememberMeDays = rememberMeDays;
        this.forgotPasswordMinutes = forgotPasswordMinutes;
        this.restClient = RestClient.builder().build();
    }

    public AuthResult signup(String name, String email, String password, String confirmPassword, boolean rememberMe) {
        validatePasswordConfirmation(password, confirmPassword);
        String normalizedEmail = normalizeEmail(email);
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new AuthException("Email already used");
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setProvider(AuthProvider.LOCAL);
        userRepository.save(user);

        return buildSession(user, rememberMe);
    }

    public AuthResult login(String email, String password, boolean rememberMe) {
        UserEntity user = userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new AuthException("This account uses social login");
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AuthException("Invalid credentials");
        }
        return buildSession(user, rememberMe);
    }

    public AuthResult googleSignin(String idToken, boolean rememberMe) {
        Map<String, Object> googlePayload;
        try {
            googlePayload = restClient.get()
                    .uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken))
                    .retrieve()
                    .body(Map.class);
        } catch (Exception ex) {
            throw new AuthException("Invalid Google token");
        }
        if (googlePayload == null || !Boolean.parseBoolean(String.valueOf(googlePayload.get("email_verified")))) {
            throw new AuthException("Google email is not verified");
        }

        String email = normalizeEmail(String.valueOf(googlePayload.get("email")));
        String googleSub = String.valueOf(googlePayload.get("sub"));
        String name = String.valueOf(googlePayload.getOrDefault("name", email));

        UserEntity user = userRepository.findByEmail(email).orElseGet(() -> {
            UserEntity created = new UserEntity();
            created.setEmail(email);
            created.setName(name);
            created.setProvider(AuthProvider.GOOGLE);
            created.setProviderId(googleSub);
            return userRepository.save(created);
        });

        if (user.getProvider() == AuthProvider.LOCAL) {
            throw new AuthException("This email is already registered with password login");
        }
        user.setProvider(AuthProvider.GOOGLE);
        user.setProviderId(googleSub);
        user.setName(name);

        return buildSession(user, rememberMe);
    }

    public void logout(String token) {
        sessionRepository.deleteByToken(token);
    }

    public String forgotPassword(String email) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(normalizeEmail(email));
        if (userOpt.isEmpty()) {
            return null;
        }

        UserEntity user = userOpt.get();
        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new AuthException("Social login account cannot reset local password");
        }

        resetTokenRepository.deleteByUser(user);
        PasswordResetTokenEntity reset = new PasswordResetTokenEntity();
        reset.setUser(user);
        reset.setToken(UUID.randomUUID().toString());
        reset.setUsed(false);
        reset.setExpiresAt(Instant.now().plus(forgotPasswordMinutes, ChronoUnit.MINUTES));
        resetTokenRepository.save(reset);

        sendResetEmail(user.getEmail(), reset.getToken());
        return reset.getToken();
    }

    public void resetPassword(String token, String newPassword, String confirmPassword) {
        validatePasswordConfirmation(newPassword, confirmPassword);
        PasswordResetTokenEntity resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("Invalid reset token"));

        if (resetToken.isUsed() || resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new AuthException("Reset token expired or already used");
        }

        UserEntity user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setProvider(AuthProvider.LOCAL);
        userRepository.save(user);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);
        sessionRepository.deleteByUser(user);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> getUserFromToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        sessionRepository.deleteByExpiresAtBefore(Instant.now());
        return sessionRepository.findByToken(token)
                .filter(session -> session.getExpiresAt().isAfter(Instant.now()))
                .map(AuthSessionEntity::getUser);
    }

    private AuthResult buildSession(UserEntity user, boolean rememberMe) {
        AuthSessionEntity session = new AuthSessionEntity();
        session.setUser(user);
        session.setRememberMe(rememberMe);
        session.setToken(UUID.randomUUID().toString() + "." + UUID.randomUUID());
        Instant expiresAt = rememberMe
                ? Instant.now().plus(rememberMeDays, ChronoUnit.DAYS)
                : Instant.now().plus(accessTokenMinutes, ChronoUnit.MINUTES);
        session.setExpiresAt(expiresAt);
        sessionRepository.save(session);
        return new AuthResult(session.getToken(), expiresAt.getEpochSecond(), user.getEmail(), user.getName());
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            throw new AuthException("Email is required");
        }
        return email.trim().toLowerCase();
    }

    private void validatePasswordConfirmation(String password, String confirmPassword) {
        if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            throw new AuthException("Password confirmation does not match");
        }
    }

    private void sendResetEmail(String email, String token) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, false);
            helper.setTo(email);
            helper.setSubject("Password Reset");
            helper.setText("Use this token to reset your password: " + token);
            mailSender.send(message);
        } catch (Exception ignored) {
            // Dev fallback: token still returned in response for manual testing.
        }
    }
}
