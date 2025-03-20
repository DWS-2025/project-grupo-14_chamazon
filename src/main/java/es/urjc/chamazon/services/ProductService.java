/*
package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ProductService {
    private final ConcurrentMap<Integer, Product> products = new ConcurrentHashMap<>();
    private int productId = 1;

    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public Product findById(int id) {
        return products.get(id);
    }
    public Product addProduct(String name, String description, double price, Category category, String image) {
        Product product = new Product();
        product.setId(productId++);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setImage(image);

        products.put(product.getId(), product);
        return product;
    }
    public void updateProduct(int id, String name, String description, double price, Category category, String image) {
        Product product = findById(id);
        if (product != null) {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
            if (image != null && !image.isEmpty()) {
                product.setImage(image);
            }
        }
    }
    public void delete(int id) {
        products.remove(id);
    }
}*/
