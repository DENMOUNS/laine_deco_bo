package cm.dolers.laine_deco.infrastructure.security;

import cm.dolers.laine_deco.application.usecase.AuthApplicationService;
import cm.dolers.laine_deco.domain.model.Role;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private final AuthApplicationService authService;

    public AuthTokenFilter(AuthApplicationService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        System.out.println(">>> FILTER PATH: " + request.getServletPath());
        System.out.println(">>> AUTH HEADER: " + request.getHeader("Authorization"));
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.getUserFromToken(token).ifPresent(this::setAuthentication);
        }
        chain.doFilter(request, response);
    }

    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    //         throws ServletException, IOException {
    //     String authHeader = request.getHeader("Authorization");
    //     if (authHeader != null && authHeader.startsWith("Bearer ")) {
    //         String token = authHeader.substring(7);
    //         authService.getUserFromToken(token).ifPresent(this::setAuthentication);
    //     }
    //     chain.doFilter(request, response);
    // }

    // @Override
    // protected boolean shouldNotFilter(HttpServletRequest request) {
    //     String path = request.getServletPath();
    //     return path.startsWith("/admin/errors");
    // }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String dispatchType = request.getDispatcherType().name();
    
    // Ignorer tous les dispatches ERROR
        if ("ERROR".equals(dispatchType)) return true;
        
        return path.startsWith("/admin/errors")
            || path.equals("/error")
            || path.equals("/favicon.ico")
            || path.startsWith("/css/")
            || path.startsWith("/js/")
            || path.startsWith("/images/");
    }

    private void setAuthentication(UserEntity user) {
        Set<Role> userRoles = user.getRoles().stream()
                .map(ur -> ur.getRole())
                .collect(Collectors.toSet());
        
        Collection<GrantedAuthority> authorities = userRoles.stream()
                .map(RoleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        var principal = new AuthenticatedUser(user.getId(), user.getEmail(), user.getName(), userRoles);
        var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
