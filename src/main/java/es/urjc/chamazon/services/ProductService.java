package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ProductService {
    private int productId = 1;
    private final CategoryService categoryService;
    private final ConcurrentMap<Integer, Product> products = new ConcurrentHashMap<>();
    private final ImageService imageService;

    public ProductService(CategoryService categoryService, ImageService imageService) {
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public Product getProduct(int id) {
    return products.get(id);
    }


    /*public String processMultipartFile(MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Files.createDirectories(IMAGES_FOLDER);
            Path imagePath = IMAGES_FOLDER.resolve(fileName);
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }
        return null;
    }*/


    public void validateProduct(Product product) {
    if (product.getName() == null || product.getName().trim().isEmpty()) {
        throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
    }
    
    if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
        throw new IllegalArgumentException("La descripción del producto no puede estar vacía");
    }

    if (product.getPrice() < 0) {
        throw new IllegalArgumentException("El precio no puede ser negativo");
    }

    if (product.getCategory() == null) {
        throw new IllegalArgumentException("La categoría no puede ser nula");
    }
    }

    public Product addProduct(String name, String description, double price, Category category, String imageName) {
        Product product = new Product();
        product.setId(productId);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setImage(imageName);

        validateProduct(product);
        
        products.put(productId, product);
        categoryService.addProductToCategory(product, category.getId());
        productId++;
        return product;
    }

    public Product addProduct(String name, String description, double price, Category category, MultipartFile imageFile) throws IOException {
        String imageName = imageService.saveImage(imageFile);
        return addProduct(name, description, price, category, imageName);
    }

    public void updateProduct(int id, String name, String description, double price, Category category, MultipartFile imageFile) throws IOException {
        Product existingProduct = products.get(id);
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product not found");
        }

        String imageName = imageService.saveImage(imageFile);

        // Remove from old category
        Category oldCategory = existingProduct.getCategory();
        if (oldCategory != null) {
            categoryService.removeProductFromCategory(id, oldCategory.getId());
        }

        // Update product
        existingProduct.setName(name);
        existingProduct.setDescription(description);
        existingProduct.setPrice(price);
        existingProduct.setCategory(category);
        if (imageName != null) {
            existingProduct.setImage(imageName);
        }

        validateProduct(existingProduct);

        
        categoryService.addProductToCategory(existingProduct, category.getId());
        products.put(id, existingProduct);
    }

    public void deleteProduct(int id) {
        Product product = products.get(id);
        if (product != null) {
            products.remove(id);
            Category category = product.getCategory();
            if (category != null) {
                categoryService.removeProductFromCategory(id, category.getId());
            }
        }
    }
}