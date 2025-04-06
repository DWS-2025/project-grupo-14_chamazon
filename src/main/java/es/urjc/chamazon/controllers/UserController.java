
package es.urjc.chamazon.controllers;

import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.ShoppingCarService;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCarService shoppingCarService;

    @GetMapping("/users")
    public String users(Model model) {
        try {
            model.addAttribute("users", userService.getAllUsers());
            return "/user/users";
        }catch (NoSuchElementException e){
            return "/error/error";
        }
    }

    @GetMapping("/users/add")
    public String addUser(Model model) {
        List<UserDTO> users = userService.getAllUsers();

        if (!users.isEmpty()) {
            model.addAttribute("user", users.get(0));
        } else {
            model.addAttribute("user");
        }
        return "/user/addUser";
    }

    @PostMapping("/users/add")
    public String addUser(UserDTO newUserDTO, Model model) {
        userService.save(newUserDTO);
        shoppingCarService.firstSC(newUserDTO);
        return "redirect:/users";
    }

    @GetMapping("user/delete")
    public String deleteUser(@RequestParam long id, Model model) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("user/delete")
    public String deleteUser(@RequestParam long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("users/edit")
    public String editUser(@RequestParam Long id, Model model) {
        try{
            UserDTO userDTO = userService.getUser(id);
            model.addAttribute("user", userDTO);
            return "/user/editUser";
        }catch (NoSuchElementException e){
            return "error/error";
        }
    }

    @PostMapping("/users/edit")
    public String editUser(@RequestParam Long id, UserDTO userDTO) {
        try {
            userService.updateUser(userDTO.id(), userDTO);
            return "redirect:/users";
        }catch (NoSuchElementException e){
            return "error/error";
        }
    }
}

