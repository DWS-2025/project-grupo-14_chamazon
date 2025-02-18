package es.urjc.chamazon.models;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


@SessionScope
public class User {
    private int id = 1;
    private String username;
    private String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getName() {
        return username;
    }
    public void setName(String providedUsername) {
        this.username = providedUsername;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String providedEmail) {
        this.email = providedEmail;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getShoppingCart() {
        return "ShoppingCart";
    }
}
