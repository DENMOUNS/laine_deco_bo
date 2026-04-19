package cm.dolers.laine_deco.infrastructure.config;

import cm.dolers.laine_deco.infrastructure.security.AuthTokenFilter;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthTokenFilter authTokenFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
    .cors(Customizer.withDefaults())
    .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .exceptionHandling(ex -> ex
        .authenticationEntryPoint((request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        })
    )
    .authorizeHttpRequests(auth -> auth
        .dispatcherTypeMatchers(
            jakarta.servlet.DispatcherType.ERROR
        ).permitAll()
        // Static resources - permit all
        .requestMatchers("/favicon.ico", "/css/**", "/js/**", "/images/**").permitAll()
        // Public API
        .requestMatchers(HttpMethod.GET, "/api/auth/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
        // Actuator
        .requestMatchers("/actuator/**").permitAll()
        // H2 Console
        .requestMatchers("/h2-console/**").permitAll()
        // Error pages
        .requestMatchers("/error", "/error/**").permitAll()
        .requestMatchers("/admin/errors", "/admin/errors/**").permitAll()
        // Swagger/OpenAPI - permit all
        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html").permitAll()
        .requestMatchers("/v3/api-docs/**", "/v3/api-docs").permitAll()
        .requestMatchers("/api-docs", "/api-docs.yaml").permitAll()
        .requestMatchers("/openapi.yaml", "/openapi.json").permitAll()
        .requestMatchers("/swagger-resources/**").permitAll()
        .requestMatchers("/webjars/**").permitAll()
        .requestMatchers("/doc.html").permitAll()
        // Protected API
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .requestMatchers("/api/finance/**").hasAnyRole("FINANCE", "ADMIN")
        .requestMatchers("/api/client/**").hasRole("CLIENT")
        .anyRequest().authenticated())
    .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
