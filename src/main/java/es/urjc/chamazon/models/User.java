package es.urjc.chamazon.models;

import jakarta.persistence.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.Objects;



@SessionScope
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String type;
    private String userName;
    private String firstName;
    private String surname;
    private String password;
    private String email;
    private String phone;
    private String address;

    //orphanRemoval -> User cant exist without shoppingCar
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCar> shoppingCarList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL )
    private List<Product> productList;


    public User() {

    }





}