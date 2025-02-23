package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class UserService {

    @Autowired
    private ShopingCarService shopingCarService;


    private int userId = 1;
    private ConcurrentMap<Integer, User> users = new ConcurrentHashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User getUser(int id) {
        return users.get(id);
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void removeAllUsers() {
        users.clear();
    }

    public void getShoppingFromUser(int id) {
        User user = users.get(id);
        if (user != null) {
            System.out.println("Carrito de " + user.getName() + ": " + shopingCarService.getShopingCarByIdUser(user.getId()));
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }
    public void addShopingToUser(int id, Product product) {
        User user = users.get(id);
        if (user != null) {
            //user.getShoppingCart().add(product);
            System.out.println("Producto añadido al carrito de " + user.getName());
        } else {
            System.out.println("Usuario no encontrado.");
        };
    }

    public void deleteUser(int id) {
        users.remove(id);
    }

    public User getUserById(int id) {
        return users.get(id);
    }
}
