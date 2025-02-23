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
    private ShoppingCarService shoppingCarService;


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
        if (shoppingCarService.getShoppingCarByIdUser(user.getId()) == null && shoppingCarService.getShoppingCarByIdUser(user.getId()).getDateSold() == null) {
            addUser(user);

        }else{
            shoppingCarService.addShoppingCarToUser(user.getId());
        }

    }

    public void removeUser(int id) {
        users.remove(id);
    }
    public void removeAllUsers() {
        users.clear();
    }

    public void getShoppingFromUser(int id) {
        User user = users.get(id);
        if (user != null) {
            System.out.println("Carrito de " + user.getName() + ": " + shoppingCarService.getShoppingCarByIdUser(user.getId()));
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

    public void deleteUser(int id) {
        users.remove(id);
    }

    public User getUserById(int id) {
        return users.get(id);
    }
}
