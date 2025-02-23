package es.urjc.chamazon.models;

import es.urjc.chamazon.services.ShoppingCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Objects;



@SessionScope
public class User {
    private int id;
    private int type;
    private String userName;
    private String name;
    private String surname;
    private String password;
    private String email;
    private String phone;
    private String address;

    /*Generate a sequence id for the new registers*/
    /*Genera un id secuencial para los nuevos registros*/
    private static int idCounter = 1;




    public User(String userName, int type, String name, String surname, String password, String email, String phone, String address) {
        this.id = generateNextId();
        this.type=type;
        this.userName = userName;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public User(String userName, String email, String password) {
        this.id = generateNextId();
        this.userName = userName;
        this.password = password;
        this.email = email;

    }
    /*Generate a sequence id for the new registers*/
    /*Genera un id secuencial para los nuevos registros*/
    private static synchronized int generateNextId() {
        return idCounter++;
    }

    /*GETTERS - SETTERS*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && type == user.type && Objects.equals(userName, user.userName) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(phone, user.phone) && Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, userName, name, surname, password, email, phone, address);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", type=" + type +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}