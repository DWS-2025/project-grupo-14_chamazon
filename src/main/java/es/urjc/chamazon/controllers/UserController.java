package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String users(Model model) {
        Collection<User> users = userService.getAllUsers();
        System.out.println(users.toString());
        model.addAttribute("users", users);
       return "users";
    }

    @GetMapping("/users/add")
    public String addUser(Model model) {
        Collection<User> users = userService.getAllUsers();
        model.addAttribute("user", users.iterator().next());
        return "addUser";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam String userName, @RequestParam String userEmail) {
        User newUser = new User(userName, userEmail);
        userService.addUser(newUser);
        return "redirect:/users";
    }
}
