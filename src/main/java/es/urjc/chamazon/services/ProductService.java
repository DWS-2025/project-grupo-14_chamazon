package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Service
public class ProductService {

    private ConcurrentMap<Integer, Product> products = new ConcurrentHashMap();
    private final CategoryService categoryService;
    private int productId = 1;

    public ProductService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public Product getProductById(int id) {
        return products.get(id);
    }

    public void createProduct(Product product) {
        Product newProduct = new Product(productId, product.getName(), product.getDescription(), product.getPrice(), product.getImage(), product.getCategory());

        newProduct.setId(productId);

        String name = product.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        newProduct.setName(name);

        String description = product.getDescription();
        if (description != null) {
            newProduct.setDescription(description);
        }

        double price = product.getPrice();
        if (price < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        newProduct.setPrice(price);

        Category category = product.getCategory();
        if (category == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        newProduct.setCategory(category);

        String image = product.getImage();
        if (image != null) {
            newProduct.setImage(image);
        }
        categoryService.addProductToCategory(newProduct, newProduct.getCategory().getId());
        products.put(productId, newProduct);
        productId++;
    }

    public Product updateProduct(int id, Product productDetails) {
        Product product = products.get(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        Category oldCategory = product.getCategory();
        if (oldCategory != null) {
        categoryService.removeProductFromCategory(product.getId(), oldCategory.getId());
        }

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());
        if (productDetails.getImage() != null) {
            product.setImage(productDetails.getImage());
        }

        categoryService.addProductToCategory(product, productDetails.getCategory().getId());

        products.put(id, product);
        return product;
    }

    public void deleteProduct(int id)
    {
        products.remove(id);
    }


    public String getProductImage(int id) {
        Product product = getProductById(id);
        if (product != null) {
            String imagePath = "src/main/es.urjc.chamazon/images/" + product.getImage();
            return imagePath;
        }
        return null;
    }
}
