package es.urjc.chamazon.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ShoppingCar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime dateSold;

    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> productList = new ArrayList<>();

    public ShoppingCar() {
    }

    public ShoppingCar(User user) {
        this.user = user;
    }

    //GETTERS AND SETTERS//


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateSold() {
        return dateSold;
    }

    public void setDateSold(LocalDateTime dateSold) {
        this.dateSold = dateSold;
    }

    public User getUser() {
        return user;
    }
    public Long getUserId() {
        return user.getId();
    }

    public void setUser(User userSc) {
        this.user = userSc;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

}
