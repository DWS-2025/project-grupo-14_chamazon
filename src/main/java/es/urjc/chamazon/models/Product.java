package es.urjc.chamazon.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String image;

    @Lob
    @Column(name = "imageFile")
    @JsonIgnore
    private Blob imageFile;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> categoryList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "productList")
    private List<ShoppingCar> shoppingCarList;

    private String originalFileName;

    private String storedFileName;

    private String filePath;


    public Product() { }

    public Product(String name, Float price, String description, Blob imageFile, Float rating) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageFile = imageFile;
        this.rating = rating;
    }


    public Product(String name, Float price, String description, Float rating) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
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

    public void setImageFile(Blob imageFile){
        this.imageFile = imageFile;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ShoppingCar> getShoppingCarList() {
        return shoppingCarList;
    }

    public void setShoppingCarList(List<ShoppingCar> shoppingCarList) {
        this.shoppingCarList = shoppingCarList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setOriginalFileName(String pdfFileName) {
        this.originalFileName = pdfFileName;
    }
    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }
    public String getStoredFileName() {
        return storedFileName;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description + ", rating=" + rating + "]";
    }

}
