package es.urjc.chamazon.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

    @ManyToMany
    private List<Product> productList;

    public Category() {

    }

    //GETTERS AND SETTERS//



}
