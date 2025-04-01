
package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.CategoryDTO;
import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.dto.UserMapper;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.repositories.ShoppingCarRepository;
import es.urjc.chamazon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class UserService {

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;


    public List<UserDTO> getAllUsers() {
        return toDTOs(userRepository.findAll());
    }

    public UserDTO getUser(Long Id) {
        Optional<User> user = userRepository.findById(Id);
        if (user.isPresent()) {
            return toDTO(user.get());
        }else{
            return null;
        }
    }

    public void deleteUser(Long Id) {
        userRepository.deleteById(Id);
    }

    public void save(UserDTO userDTO) {
        User user = toUser(userDTO);
        userRepository.save(user);
        //falta añadir la parte del carrito
    }
    public void updateUser(Long id, UserDTO updatedUserDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = toUser(updatedUserDTO);
            user.setId(id);
            userRepository.save(user);
        }
    }

    private UserDTO toDTO(User user){
        return userMapper.toDTO(user);
    }
    private User toUser(UserDTO userDTO){
        return userMapper.toUser(userDTO);
    }
    private List<UserDTO> toDTOs(List<User> users){
        return userMapper.toDTOs(users);
    }
    private List<User> toUsers(List<UserDTO> usersDTO){
        return userMapper.toUsers(usersDTO);
    }




    /*
    public void addUser(User user) {
        users.put(user.getId(), user);
        shoppingCarService.addNewSoppingCarToUser(user.getId());
    }


    public void getShoppingFromUser(int id) {
        User user = users.get(id);
        if (user != null) {
            System.out.println("Carrito de " + user.getName() + ": " + shoppingCarService.getActualShoppingCarByIdUser(user.getId()));
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }
    public void addShoppingToUser(int id, Product product) {
        User user = users.get(id);
        if (user != null) {
            //user.getShoppingCart().add(product);
            System.out.println("Producto añadido al carrito de " + user.getName());
        } else {
            System.out.println("Usuario no encontrado.");
        };
    }

    public void updateUser(User existingUser) {
        users.put(existingUser.getId(), existingUser);
    }*/
}

