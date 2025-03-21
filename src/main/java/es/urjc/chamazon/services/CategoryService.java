
package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    public Collection<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }




    /*
    public void addCategory(String categoryName) {
        Category newCategory = new Category(categoryName);
        newCategory.setId(categoryId++);
        categories.put(newCategory.getId(), newCategory);
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

    public void updateCategory(Category category) {
        categories.put(category.getId(), category);
    }*/
}

