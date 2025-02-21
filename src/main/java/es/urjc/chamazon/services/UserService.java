package es.urjc.chamazon.services;

import es.urjc.chamazon.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class UserService {

    private ConcurrentMap<Integer, User> users = new ConcurrentHashMap<>();

    public User getUserById(int id) {
        return users.get(id);
    }
    public void addUser(User user) {
        users.put(user.getId(), user);
    }
    public Collection<User> getUsers() {
        return users.values().;
    }

    public void deleteUser(int id) {
        users.remove(id);
    }

    public void updateUser(User user) {
        users.replace(user.getId(), user);
    }

    public boolean existUser(int id) {
        return users.containsKey(id);
    }

    public void clearUsers() {
        users.clear();
    }

    public int getSize() {
        return users.size();
    }






}
