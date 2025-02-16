package es.urjc.chamazon.models;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class Category {
    private String name;
    private int id;
    private ConcurrentMap<Integer, Product> products;

    public Category(String name) {
        this.name = name;
        this.products = new ConcurrentHashMap<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public Collection<Product> getAllProducts(){
        return products.values();
    }

    public Product getProduct(int id){
        return products.get(id);
    }

    public void addProduct(Product productToAdd){
        products.put(productToAdd.getId(), productToAdd);
    }

    public void removeProduct(int productId){
        products.remove(productId);
    }

}
