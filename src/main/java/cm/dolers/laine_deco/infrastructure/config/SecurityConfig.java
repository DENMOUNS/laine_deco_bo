package cm.dolers.laine_deco.infrastructure.config;

import cm.dolers.laine_deco.infrastructure.security.AuthTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig
 *
 * CORRECTIONS :
 * - authenticationEntryPoint retourne JSON (pas HTML) → pour que le dashboard
 *   monitoring reçoive bien les 401 en JSON
 * - accessDeniedHandler retourne JSON pour les 403
 * - Les erreurs 401/403 sont désormais interceptées par GlobalExceptionHandler
 *   via les handlers configurés ici
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthTokenFilter authTokenFilter)
            throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(ex -> ex
                // CORRECTION : Retourne JSON pour les 401 (pas de redirect, pas de HTML)
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write(
                        "{\"status\":401,\"error\":\"UNAUTHORIZED\"," +
                        "\"message\":\"Token manquant ou invalide\"," +
                        "\"path\":\"" + request.getRequestURI() + "\"}"
                    );
                })
                // CORRECTION : Retourne JSON pour les 403 (pas de HTML)
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write(
                        "{\"status\":403,\"error\":\"FORBIDDEN\"," +
                        "\"message\":\"Accès refusé — droits insuffisants\"," +
                        "\"path\":\"" + request.getRequestURI() + "\"}"
                    );
                })
            )
            .authorizeHttpRequests(auth -> auth
                // Dispatcher error (toujours en premier)
                .dispatcherTypeMatchers(jakarta.servlet.DispatcherType.ERROR).permitAll()

                // ===== STATIC =====
                .requestMatchers("/favicon.ico", "/css/**", "/js/**",
                        "/images/**", "/webjars/**").permitAll()

                // ===== MONITORING ADMIN =====
                .requestMatchers("/admin/errors", "/admin/errors/**").permitAll()

                // ===== ACTUATOR =====
                .requestMatchers("/actuator/**").permitAll()

                // ===== H2 Console (dev only) =====
                .requestMatchers("/h2-console/**").permitAll()

                // ===== ERROR =====
                .requestMatchers("/error", "/error/**").permitAll()

                // ===== SWAGGER =====
                .requestMatchers(
                    "/swagger-ui/**", "/swagger-ui.html",
                    "/v3/api-docs", "/v3/api-docs/**",
                    "/api-docs", "/api-docs/**", "/api-docs.yaml",
                    "/openapi.yaml", "/swagger-resources/**"
                ).permitAll()

                // ===== AUTH =====
                .requestMatchers(HttpMethod.GET, "/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                // ===== PUBLIC API =====
                .requestMatchers("/api/public/**").permitAll()

                // ===== PROTECTED =====
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/finance/**").hasAnyRole("FINANCE", "ADMIN")
                .requestMatchers("/api/client/**").hasRole("CLIENT")

                .anyRequest().authenticated()
            )
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
