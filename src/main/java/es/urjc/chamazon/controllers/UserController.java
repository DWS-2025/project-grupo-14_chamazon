
package es.urjc.chamazon.controllers;

import es.urjc.chamazon.configurations.SecurityUtils;
import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.dto.UserDTOExtended;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.CommentService;
import es.urjc.chamazon.services.SecurityService;
import es.urjc.chamazon.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;


import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private SecurityService securityService;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO(null, "", "", "", "", "", "", ""));
        return "user/register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {

        if (userDTO.userName() == null || userDTO.userName().isBlank()) {
            model.addAttribute("error", "El nombre de usuario es obligatorio");
            return "user/register";
        }
        if (userDTO.password() == null || userDTO.password().isBlank()) {
            model.addAttribute("error", "La contraseña es obligatoria");
            return "user/register";
        }
        if (!userDTO.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Email inválido");
            return "user/register";
        }

        if (userService.userNameExists(userDTO.userName())) {
            model.addAttribute("error", "Ten más imaginación, el nombre de usuario ya está en uso");
            return "user/register";
        }

        userService.save(userDTO);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String users(Model model) {
        try {
            model.addAttribute("users", userService.getAllUsers());
            return "/user/users";
        }catch (NoSuchElementException e){
            return "/error";
        }
    }

    @GetMapping("/user")
    public String user(Model model, HttpServletRequest request) {
        try {
            securityService.permission();

        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("errorCode", "403");
            return "error";
        }

        try{
            UserDTOExtended userDTOExtended = userService.findByUserName(SecurityUtils.getCurrentUsername()).orElseThrow();
            model.addAttribute("user", userDTOExtended);
            return "/user/userIndividual";
        }catch (NoSuchElementException e){
            return "/error";
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
        model.addAttribute("user", newUserDTO);

        if (newUserDTO.firstName() == null || newUserDTO.firstName().trim().isEmpty()) {
            model.addAttribute("error", "El nombre es obligatorio.");
            return "/user/addUser";
        }

        if (newUserDTO.surName() == null || newUserDTO.surName().trim().isEmpty()) {
            model.addAttribute("error", "El apellido es obligatorio.");
            return "/user/addUser";
        }

        if (newUserDTO.userName() == null || newUserDTO.userName().trim().isEmpty()) {
            model.addAttribute("error", "El usuario es obligatorio.");
            return "/user/addUser";
        }

        if (newUserDTO.email() == null || newUserDTO.email().trim().isEmpty() ||
                !newUserDTO.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "El email es obligatorio y debe ser válido.");
            return "/user/addUser";
        }

        if (newUserDTO.password() == null || newUserDTO.password().trim().isEmpty()) {
            model.addAttribute("error", "La contraseña es obligatoria.");
            return "/user/addUser";
        }

        if (userService.userNameExists(newUserDTO.userName())) {
            model.addAttribute("error", "Ten más imaginación, el nombre de usuario ya está en uso");
            return "user/register";
        }

        userService.saveNewUser(newUserDTO);
        return "redirect:/users";
    }

    @GetMapping("/user/delete")
    public String deleteUser(@RequestParam long id, HttpServletRequest request, Model model) {
        try {
            securityService.permission(id);

        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("errorCode", "403");
            return "error";
        }

        boolean isAdmin = SecurityUtils.isAdmin();
        userService.deleteUser(id, request);
        commentService.deleteCommentsWithoutUser();
        if (isAdmin) {
            return "redirect:/users";
        }else{
            return "redirect:/";
        }

    }


    /*@PostMapping("user/delete")
    public String deleteUser(@RequestParam long id, HttpServletRequest request) {
        userService.deleteUser(id, request);
        commentService.deleteCommentsWithoutUser();
        return "redirect:/";
    }*/

    @GetMapping("users/edit")
    public String editUser(@RequestParam Long id, Model model) {
        try {
            securityService.permission(id);

        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("errorCode", "403");
            return "error";
        }

        try{
            UserDTO userDTO = userService.getUser(id);
            model.addAttribute("user", userDTO);
            return "/user/editUser";
        }catch (NoSuchElementException e){
            return "/error";
        }
    }

    @PostMapping("/users/edit")
    public String editUser(@RequestParam Long id, UserDTO userDTO, Model model) {
        try {
            securityService.permission(id);

        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("errorCode", "403");
            return "error";
        }

        boolean isAdmin = SecurityUtils.isAdmin();
        try {
            userService.updateUser(userDTO.id(), userDTO);
            if (isAdmin) {
                return "redirect:/users";
            }else{
                return "redirect:/user";
            }
        }catch (NoSuchElementException e){
            return "/error";
        }
    }
}

