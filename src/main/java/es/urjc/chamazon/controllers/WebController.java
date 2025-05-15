package es.urjc.chamazon.controllers;

import es.urjc.chamazon.configurations.SecurityUtils;
import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.dto.UserDTOExtended;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class WebController {

    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

/*    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            model.addAttribute("userLink", SecurityUtils.isAdmin() ? "/users" : "/user");
            model.addAttribute("userName", principal.getName());
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
            //model.addAttribute("idUser", userService.findByUserName(principal.getName()).ifPresent(null).id());
            userService.findByUserName(principal.getName()).ifPresent(user ->
                    model.addAttribute("idUser", user.id()));
        }else{
            model.addAttribute("isAuthenticated",false);
        }

    }*/


/*
    public void addAttributes(Model model) {
        model.addAttribute("userLink",SecurityUtils.isAdmin() ? "/users" : "/user");
        model.addAttribute("isAdmin", SecurityUtils.isAdmin());
        model.addAttribute("isAuthenticated", SecurityUtils.isAuthenticated());
        String username = SecurityUtils.getCurrentUsername();
        userService.findByUserName(username).ifPresent(user ->
                model.addAttribute("idUser", user.id())
        );
    }
    }*/

    @GetMapping("/")
    public String index() {
        return "main";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginerror")
    public String loginerror(Model model) {
        model.addAttribute("error", "Login failed. Please check your credentials.");
        return "loginerror";
    }

    //FOR EXAMPLES
    @GetMapping("/private")
    public String privatePage(Model model, HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();
        UserDTOExtended user = userService.findByUserName(name).get();
        model.addAttribute("username", user.userName());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        return "private";
    }
}
