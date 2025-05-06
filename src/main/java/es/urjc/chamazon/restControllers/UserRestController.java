package es.urjc.chamazon.restControllers;


import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("api/users")
public class UserRestController {
    @Autowired
    UserService userService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id){
        UserDTO user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        userService.saveNewUser(userDTO);
        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(userDTO).toUri();
        return ResponseEntity.created(location).body(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO){
        userService.updateUser(id, userDTO);
        return ResponseEntity.ok(userService.getUser(id));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(userService.getUser(id));
    }
}
