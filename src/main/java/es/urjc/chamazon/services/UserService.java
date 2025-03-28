
package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.repositories.ShoppingCarRepository;
import es.urjc.chamazon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class UserService {

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

    @Autowired
    private UserRepository userRepository;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(long Id) {
        return userRepository.findById(Id);
    }

    public void deleteById(long Id) {
        userRepository.deleteById(Id);
    }

    public void save(User user) {
        userRepository.save(user);
        //falta añadir la parte del carrito
    }
    public void updateUser(Long id, String userName, String password, String email, String phone, String address) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setUserName(userName);
            existingUser.setEmail(email);
            existingUser.setAddress(address != null ? address : "");
            existingUser.setPassword(password);
            existingUser.setPhone(phone != null ? phone : "");
            userRepository.save(existingUser);
        }
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

