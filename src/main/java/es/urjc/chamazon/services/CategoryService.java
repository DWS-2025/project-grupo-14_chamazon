package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import org.springframework.beans.AbstractNestablePropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
    private static List<Category> categories = new ArrayList<>();
    static{
        // Crear categorías
        Category electronica = new Category("Electronica");
        Category ropa = new Category("Ropa");
        Category hogar = new Category("Hogar");

        // Crear productos de prueba y asociarlos a categorías
        Product p1 = new Product(1, "Laptop", "Laptop de alta gama", 1000.0, null, electronica);
        Product p2 = new Product(2, "Camiseta", "Camiseta de algodón", 20.0, null, ropa);
        Product p3 = new Product(3, "Sofá", "Sofá cómodo", 500.0, null, hogar);

        // Asociar productos a categorías
        electronica.addProduct(p1);
        ropa.addProduct(p2);
        hogar.addProduct(p3);

        // Agregar categorías a la lista
        categories.add(electronica);
        categories.add(ropa);
        categories.add(hogar);
    }

    public List<Category> getAllCategories() {
        return categories;
    }

    public void addCategory(String categoryName) {
        categories.add(new Category(categoryName));
    }

    public void removeCategory(String category) {
        categories.removeIf(c -> c.getName().equals(category));
    }

    public void addProductToCategory(Product product, Category category) {
        for (Category c : categories) {
            if (c.equals(category)){
                c.addProduct(product);
            }
        }
    }

    public void removeProductFromCategory(Product product, Category category) {
        for (Category c : categories) {
            if (c.equals(category)){
                c.removeProduct(product);
            }
        }
    }
    public List<Product> getProductsFromCategory(String category) {
        for (Category c : categories) {
            if (c.getName().equalsIgnoreCase(category)){
                return c.getAllProducts();
            }
        }
        return new ArrayList<>();
    }

    public Category getCategoryByName(String categoryName) {
        for (Category c : categories) {
            if (c.getName().equalsIgnoreCase(categoryName)){
                return c;
            }
        }
        return null;
    }
}
