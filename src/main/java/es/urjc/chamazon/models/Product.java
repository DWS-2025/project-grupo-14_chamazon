package es.urjc.chamazon.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Float price;
    private String description;
    private String image;
    private Float rating;


    @ManyToOne
    private User user;

    @ManyToMany
    private List<Category> categoryList;

    @ManyToMany
    private List<ShoppingCar> shoppingCarList;

    public Product() {

    }


}
