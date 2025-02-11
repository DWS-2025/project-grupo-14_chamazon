package es.urjc.chamazon.models;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private int id;
    private String name;
    private Map<Integer, Product> products;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.products = new HashMap<>();
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
    public Map<Integer, Product> getAllProducts(){
        return products;
    }

    public Product getProduct(int id){
        return products.get(id);
    }
    public void addProduct(Product product){
        products.put(getId(), product);
    }
    public void removeProduct(int id){
        products.remove(id);
    }


}
