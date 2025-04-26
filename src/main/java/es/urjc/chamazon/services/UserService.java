
package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.dto.UserMapper;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.models.ShoppingCar;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShoppingCarService shoppingCarService;



    public List<UserDTO> getAllUsers() {
        return toDTOs(userRepository.findAll());
    }

    public UserDTO getUser(Long Id) {
        return toDTO(userRepository.findById(Id).orElseThrow());
    }

    User getUserById(Long Id) {
        Optional<User> userOpt = userRepository.findById(Id);
        return userOpt.orElse(null);
    }

    public UserDTO findByUserName(String name) {
        return toDTO(userRepository.findByUserName(name).orElseThrow());
    }

    public void deleteUser(Long id) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            for (Comment comment : user.getComments()) {
                comment.setUser(null);
            }
            user.getComments().clear();

            userRepository.save(user);

            userRepository.delete(user);
        }
    }


    public void save(UserDTO userDTO) {
        User user = toUser(userDTO);
        saveUser(user);

    }
    void saveUser(User user) {
        userRepository.save(user);
        shoppingCarService.assignSCToUser(user);
    }

    public void updateUser(Long id, UserDTO updatedUserDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = toUser(updatedUserDTO);
            userOptional.get().setId(id);
            userOptional.get().setUserName(user.getUserName());
            userOptional.get().setPassword(user.getPassword());
            userOptional.get().setEmail(user.getEmail());
            userOptional.get().setPhone(user.getPhone());
            userOptional.get().setAddress(user.getAddress());
            userRepository.save(userOptional.get());
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

