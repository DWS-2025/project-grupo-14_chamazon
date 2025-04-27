package es.urjc.chamazon.controllers;

import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.dto.UserDTOExtended;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class WebController {

    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "main";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginerror")
    public String loginerror() {
        return "loginerror";
    }
    @GetMapping("/private")
    public String privatePage(Model model, HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();
        UserDTOExtended user = userService.findByUserName(name);
        model.addAttribute("username", user.userName());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        return "private";
    }
}
