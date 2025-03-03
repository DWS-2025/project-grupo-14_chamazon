package es.urjc.chamazon.models;


import org.springframework.core.io.Resource;

import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Product {
    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");
    private int id;
    private String name;
    private String description;
    private double price;
    private String image;
    private Category category;

    public Product() {

    }

    public Product(int id, String name, String description, double price, String image, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return IMAGES_FOLDER+"\\"+image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
