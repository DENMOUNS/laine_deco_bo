package cm.dolers.laine_deco.infrastructure.config;

import cm.dolers.laine_deco.infrastructure.security.AuthTokenFilter;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        .requestMatchers(
            "/api/auth/**",
            "/actuator/health",
            "/h2-console/**",
            "/admin/errors",
            "/admin/errors/**",
            "/error",
            "/error/**",
            "/favicon.ico",
            "/css/**",
            "/js/**",
            "/images/**"
        ).permitAll()
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .requestMatchers("/api/finance/**").hasAnyRole("FINANCE", "ADMIN")
        .requestMatchers("/api/client/**").hasRole("CLIENT")
        .anyRequest().authenticated())
    .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
