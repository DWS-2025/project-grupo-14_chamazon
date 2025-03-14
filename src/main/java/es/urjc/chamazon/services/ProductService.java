package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import es.urjc.chamazon.repositories.ProductRepository;

@Service
public class ProductService {

    //comment these
    private final ConcurrentMap<Long, Product> products = new ConcurrentHashMap<>();
    //private int productId = 1;

    @Autowired ProductRepository productRepository;

    public Collection<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    //Fix this for JPA adding prodyucts and updating
    /*
      public Product addProduct(String name, String description, double price, Category category, String image) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setImage(image);

        //make sure it's right or not
        return productRepository.save(product);
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
     */
   //BETA, MAKE SURE TO FIX THIS ACCORDING TO JPA REQUIREMENTS
    public Product save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        
        validateProduct(product);
        return productRepository.save(product);
    }

    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        
        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del producto no puede estar vacía");
        }
        
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        
        if (product.getImage() == null) {
            throw new IllegalArgumentException("La imagen no puede ser nula");
        }
    }

    public void delete(long id) {
        productRepository.deleteById(id);
    }
}