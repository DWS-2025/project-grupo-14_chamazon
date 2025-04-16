package es.urjc.chamazon.models;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String comment;
    private int rating;

    @ManyToOne  // Many comments can be made by the same user and each comment is made by a user
    private User user;

    @ManyToOne   // Many comments can be made about the same product
    private Product product;

    public Comment() {
    }

    public Comment(String comment, int rating, User user, Product product) {
        this.comment = comment;
        this.rating = rating;
        this.user = user;
        this.product = product;
    }

    //GETTERS AND SETTERS//
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
