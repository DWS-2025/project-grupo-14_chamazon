package es.urjc.chamazon.configurations;

import es.urjc.chamazon.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class SecurityUtils {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAuthenticated() {
        Authentication auth = getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    public static boolean isAdmin() {
        Authentication auth = getAuthentication();
        return isAuthenticated() && auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }

    public static String getCurrentUsername() {
        return isAuthenticated() ? getAuthentication().getName() : null;
    }

    public static Long getCurrentUserId(UserRepository userRepository) {
        if (isAuthenticated()) {
            String username = getAuthentication().getName();
            return userRepository.findByUserName(username)
                    .map(es.urjc.chamazon.models.User::getId)
                    .orElse(null);
        }
        return null;
    }



    public static void logout(HttpServletRequest request) {
        if (request != null) {
            request.getSession().invalidate();
        }
        SecurityContextHolder.clearContext();
    }
}
