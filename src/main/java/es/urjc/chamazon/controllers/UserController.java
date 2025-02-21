package es.urjc.chamazon.controllers;


import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String users(Model model) {
        Collection<User> users = userService.getUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam String name,@RequestParam String password, @RequestParam String email) {
        User user = new User(name, password, email);
        userService.addUser(user);
        return "redirect:/users";
    }

    @PostMapping("user/delete")
    public String deleteUser(@RequestParam int id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("users/edit")
    public String editUser(@RequestParam int id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "editUser";}

    @GetMapping("/")
    public String home() {
        return "main";
    }

}
