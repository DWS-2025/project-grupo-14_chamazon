
package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class UserService {

    /*@Autowired
    private ShoppingCarService shoppingCarService;*/

    @Autowired
    private UserRepository userRepository;


    public Collection<User> findAll() {
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

