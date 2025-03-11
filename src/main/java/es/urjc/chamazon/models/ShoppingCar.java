package es.urjc.chamazon.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class ShoppingCar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime dateSold;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "shoppingCarList")
    private List<Product> productList;

    public ShoppingCar() {
    }


    //GETTERS AND SETTERS//


}
