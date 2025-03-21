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

    @ManyToMany()
    private List<Category> categoryList;

    @ManyToMany
    private List<ShoppingCar> shoppingCarList;

    public Product() {

    }

    //GETTERS AND SETTERS//

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userPr) {
        this.user = userPr;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<ShoppingCar> getShoppingCarList() {
        return shoppingCarList;
    }

    public void setShoppingCarList(List<ShoppingCar> shoppingCarList) {
        this.shoppingCarList = shoppingCarList;
    }
}
