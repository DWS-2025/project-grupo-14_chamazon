package es.urjc.chamazon.controllers;

import es.urjc.chamazon.configurations.SecurityUtils;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

//@ControllerAdvice
public class GlobalModelAttributes {

/*    @Autowired
    UserService userService;*/

/*    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("userLink",SecurityUtils.isAdmin() ? "/users" : "/user");
        model.addAttribute("isAdmin", SecurityUtils.isAdmin());
        model.addAttribute("isAuthenticated", SecurityUtils.isAuthenticated());
        String username = SecurityUtils.getCurrentUsername();
        if (username != null) {
            model.addAttribute("idUser", userService.findByUserName(username).id());
        }
    }*/

    /*private static String getUserLinkByRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return "/users";
            } else {
                return "/user";
            }
        }
        return "";
    }


    private boolean isCurrentUserAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return false;
    }*/

}
