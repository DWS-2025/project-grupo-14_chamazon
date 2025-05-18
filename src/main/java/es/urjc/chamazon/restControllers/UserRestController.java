package es.urjc.chamazon.restControllers;

import es.urjc.chamazon.configurations.SecurityUtils;
import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.dto.UserDTOExtended;
import es.urjc.chamazon.dto.UserIndividualDTO;
import es.urjc.chamazon.services.CommentService;
import es.urjc.chamazon.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    // === ADMIN: Obtener todos los usuarios ===
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers(Authentication auth) {
        if (!SecurityUtils.isAdmin()) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // === USER o ADMIN: Obtener un usuario por ID (pero solo si es él mismo o admin) ===
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id, Authentication auth) {
        boolean isAdmin = SecurityUtils.isAdmin();
        String username = auth.getName();
        Optional<UserDTOExtended> currentUser = userService.findByUserName(username);

        if (currentUser.isEmpty() || (!isAdmin && currentUser.get().id() != id)) {
            return ResponseEntity.status(403).body("No autorizado para acceder a este usuario");
        }

        try {
            UserIndividualDTO user = userService.getUserIndividual(id);
            return ResponseEntity.ok(user);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === USER o ADMIN: Obtener el usuario actual ===
    @GetMapping("/me")
    public ResponseEntity<UserDTOExtended> getCurrentUser(Authentication auth) {
        try {
            String username = auth.getName();
            Optional<UserDTOExtended> user = userService.findByUserName(username);
            return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // === Registro de nuevos usuarios (público) ===
    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        if (userDTO.userName() == null || userDTO.userName().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre de usuario es obligatorio");
        }
        if (userDTO.password() == null || userDTO.password().isBlank()) {
            return ResponseEntity.badRequest().body("La contraseña es obligatoria");
        }
        if (!userDTO.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body("Email inválido");
        }
        if (userService.userNameExists(userDTO.userName())) {
            return ResponseEntity.badRequest().body("Nombre de usuario ya en uso");
        }

        userService.saveNewUser(userDTO);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(userDTO.id()).toUri();
        return ResponseEntity.created(location).body(userDTO);
    }

    // === USER o ADMIN: Actualizar un usuario (solo a sí mismo o admin) ===
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO, Authentication auth) {
        boolean isAdmin = SecurityUtils.isAdmin();
        String username = auth.getName();
        Optional<UserDTOExtended> currentUser = userService.findByUserName(username);

        if (currentUser.isEmpty() || (!isAdmin && currentUser.get().id() != id)) {
            return ResponseEntity.status(403).body("No autorizado para modificar este usuario");
        }

        try {
            userService.updateUser(id, userDTO);
            return ResponseEntity.ok(userService.getUser(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === USER o ADMIN: Eliminar un usuario (solo a sí mismo o admin) ===
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id, HttpServletRequest request, Authentication auth) {
        boolean isAdmin = SecurityUtils.isAdmin();
        String username = auth.getName();
        Optional<UserDTOExtended> currentUser = userService.findByUserName(username);

        if (currentUser.isEmpty() || (!isAdmin && currentUser.get().id() != id)) {
            return ResponseEntity.status(403).body("No autorizado para eliminar este usuario");
        }

        try {
            userService.deleteUser(id, request);
            commentService.deleteCommentsWithoutUser();
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
