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
    private String image;
    private Float rating;
    private Blob imageFile;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Category> categoryList;

    @ManyToMany
    private List<ShoppingCar> shoppingCarList;

    public Product() { }

    public Product(String name, Float price, String description, String image, Float rating) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
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

    public String getImage() {
        return image;
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

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageFile(){
        this.imageFile = imageFile;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
