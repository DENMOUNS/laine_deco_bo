package cm.dolers.laine_deco.infrastructure.config;

import cm.dolers.laine_deco.infrastructure.security.AuthTokenFilter;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthTokenFilter authTokenFilter) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // Pour H2 console
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                })
            )
            .authorizeHttpRequests(auth -> auth
                // Dispatcher error
                .dispatcherTypeMatchers(
                    jakarta.servlet.DispatcherType.ERROR
                ).permitAll()

                // ===== STATIC RESOURCES =====
                .requestMatchers("/favicon.ico", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                // ===== MONITORING ADMIN (sans auth pour debug) =====
                .requestMatchers("/admin/errors", "/admin/errors/**").permitAll()

                // ===== ACTUATOR =====
                .requestMatchers("/actuator/**").permitAll()

                // ===== H2 Console =====
                .requestMatchers("/h2-console/**").permitAll()

                // ===== ERROR PAGES =====
                .requestMatchers("/error", "/error/**").permitAll()

                // ===== SWAGGER / OPENAPI - TOUS les chemins possibles =====
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-ui-custom.html",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/api-docs",
                    "/api-docs/**",
                    "/api-docs.yaml",
                    "/openapi.yaml",
                    "/openapi.json",
                    "/swagger-resources/**",
                    "/swagger-resources",
                    "/configuration/**"
                ).permitAll()

                // ===== PUBLIC AUTH =====
                .requestMatchers(HttpMethod.GET, "/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                // ===== PUBLIC API =====
                .requestMatchers("/api/public/**").permitAll()

                // ===== PROTECTED API =====
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/finance/**").hasAnyRole("FINANCE", "ADMIN")
                .requestMatchers("/api/client/**").hasRole("CLIENT")

                .anyRequest().authenticated()
            )
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}