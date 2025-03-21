package es.urjc.chamazon.models;

import java.util.List;
import java.sql.Blob;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long id;
    private String name;
    private Float price;
    private String description;
    private Float rating;

    @Lob
    private Blob imageFile;


    @ManyToOne
    private User user;

    @ManyToMany()
    private List<Category> categoryList;

    @ManyToMany
    private List<ShoppingCar> shoppingCarList;

    public Product() { }

    public Product(String name, Float price, String description, Blob imageFile, Float rating) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageFile = imageFile;
        this.rating = rating;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public Float getRating() {
        return rating;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageFile(Blob imageFile){
        this.imageFile = imageFile;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + ", rating=" + rating + "]";
    }

}
