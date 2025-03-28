
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
import java.util.List;

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



    /*


    @GetMapping("users/edit")
    public String editUser(@RequestParam int id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "editUser";}

    @PostMapping("/users/edit")
    public String editUser(@ModelAttribute("user") User user, Model model) {
        User existingUser = userService.getUserById(user.getId());
        existingUser.setUserName(user.getUserName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress() != null ? user.getAddress() : "");
        existingUser.setPassword(user.getPassword());
        existingUser.setPhone(user.getPhone() != null ? user.getPhone() : "");
        userService.updateUser(existingUser);
        return "redirect:/users";
    }*/
}

