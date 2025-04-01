package es.urjc.chamazon.models;

import jakarta.persistence.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.Objects;



@SessionScope
@Entity(name = "USR")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String type;
    private String userName;
    private String firstName;
    private String surname;
    private String password;
    private String email;
    private String phone;
    private String address;

    //orphanRemoval -> User cant exist without shoppingCar
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ShoppingCar> shoppingCarList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Product> productList;


    public User() {

    }

    public User(String type, String userName, String firstName, String surname, String password, String email, String phone, String address) {
        this.type = type;
        this.userName = userName;
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.email = (email != null) ? email : "no-email@example.com";  // Evita nulo        this.phone = phone;
        this.address = address;
        this.phone = phone;
    }


    //GETTERS AND SETTERS//


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ShoppingCar> getShoppingCarList() {
        return shoppingCarList;
    }

    public void setShoppingCarList(List<ShoppingCar> shoppingCarList) {
        this.shoppingCarList = shoppingCarList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
