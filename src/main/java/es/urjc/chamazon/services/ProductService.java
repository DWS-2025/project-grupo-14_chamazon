package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService {

    private List<Product> products = new ArrayList<>();
    private int id = 1;

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public Product createProduct(Product product) {
        Product newProduct = new Product(id, product.getName(), product.getDescription(), product.getPrice(), product.getImage(), product.getCategory());

        newProduct.setId(id);

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

        byte[] image = product.getImage();
        if (image != null) {
            newProduct.setImage(image);
        }

        id++;
        products.add(newProduct);

        return newProduct;
    }

    public Product updateProduct(int id, Product productDetails) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                Product existingProduct = products.get(i);

                String newName = productDetails.getName();
                if (newName != null && !newName.trim().isEmpty()) {
                    existingProduct.setName(newName);
                }


                String newDescription = productDetails.getDescription();
                if (newDescription != null) {
                    existingProduct.setDescription(newDescription);
                }


                double newPrice = productDetails.getPrice();
                if (newPrice >= 0) {
                    existingProduct.setPrice(newPrice);
                }


                Category newCategory = productDetails.getCategory();
                if (newCategory != null) {
                    existingProduct.setCategory(newCategory);
                }


                byte[] newImage = productDetails.getImage();
                if (newImage != null) {
                    existingProduct.setImage(newImage);
                }

                return existingProduct;
            }
        }
        return null;
    }

    public boolean deleteProduct(int id) {
        return products.removeIf(p -> p.getId() == id);
    }


    public byte[] getProductImage(int id) {
        Product product = getProductById(id);
        if (product != null) {
            String imagePath = "src/main/es.urjc.chamazon/images/" + product.getImage();
            try {
                return Files.readAllBytes(Paths.get(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
