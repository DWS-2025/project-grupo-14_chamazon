
package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
       return "/user/users";
    }

    @GetMapping("/users/add")
    public String addUser(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("user", users.iterator().next());
        return "/user/addUser";
    }

    @PostMapping("/users/add")
    public String addUser(User newUser, Model model) {
        System.out.println(newUser.getUserName());
        System.out.println(newUser.getFirstName());
        System.out.println(newUser.getId());
        System.out.println(newUser.getEmail());

        userService.save(newUser);
        return "redirect:/users";
    }

    @GetMapping("user/delete")
    public String deleteUser(@RequestParam long id, Model model) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @PostMapping("user/delete")
    public String deleteUser(@RequestParam long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("users/edit")
    public String editUser(@RequestParam Long id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/editUser";
        }else{
            return "error/error";
        }
    }

    @PostMapping("/users/edit")
    public String editUser(@RequestParam Long id, @RequestParam String userName, @RequestParam String password, @RequestParam String email, @RequestParam(required = false) String phone, @RequestParam(required = false) String address,  Model model) {
        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            userService.updateUser(id, userName, password, email, phone, address);
            return "redirect:/users";
        }else {
            return "error/error";
        }
    }
}

