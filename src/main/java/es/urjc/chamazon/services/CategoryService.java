package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class CategoryService {
    private int categoryId = 1;
    private ConcurrentMap<Integer, Category> categories = new ConcurrentHashMap<>();

    public Collection<Category> getAllCategories() {
        return categories.values();
    }

    public Category getCategoryById(int categoryId) {
        return categories.get(categoryId);
    }

    public Category getCategoryByName(String categoryName) {
        for (Category c : categories.values()) {
            if (c.getName().equalsIgnoreCase(categoryName)){
                return c;
            }
        }
        return null;
    }


    public void addCategory(String categoryName) {
        Category newCategory = new Category(categoryName);
        newCategory.setId(categoryId++);
        categories.put(newCategory.getId(), newCategory);
    }

    public void deleteCategory(int category) {
        categories.remove(category);
    }

    public void addProductToCategory(Product product, int categoryId) {
        Category categoryToAdd = categories.get(categoryId);
        if (categoryToAdd != null) {
            categoryToAdd.addProduct(product);
        }
    }

    public void removeProductFromCategory(int productId, int categoryId) {
        Category categoryToDelete = categories.get(categoryId);
        if (categoryToDelete != null) {
            categoryToDelete.removeProduct(productId);
        }
    }

    public Collection<Product> getProductsFromCategory(int category) {
        Category categoryToSearch = categories.get(category);
        if (categoryToSearch != null) {
            return categoryToSearch.getAllProducts();
        }
        return Collections.emptyList();
    }

    public Integer getCategoryIdByProductId(int productId) {
        for (Category category : categories.values()) {
            System.out.println("Revisando categoría " + category.getId());
            if (category.getProduct(productId) != null) {
                System.out.println("Producto " + productId + " encontrado en categoría " + category.getId());
                return category.getId();
            }
        }
        System.out.println("Producto " + productId + " no encontrado en ninguna categoría");
        return null;
    }

}
