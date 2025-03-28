
package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.repositories.CategoryRepository;
import es.urjc.chamazon.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;


    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public void save(Category category) {
         categoryRepository.save(category);
    }

    public void deleteById(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()){
            for (Product product : new ArrayList<>(category.get().getProductList())) {
                product.getCategoryList().remove(category.get());
                productService.save(product); // Actualizamos el producto
            }

            category.get().getProductList().clear();
            categoryRepository.save(category.get()); // Guardamos los cambios

            categoryRepository.delete(category.get());
        }

    }

    public Object getProductsByCategoryId(Long id) {
        return categoryRepository.findProductsByCategoryId(id);
    }

    public void addProductToCategory(Long categoryId, Long productId) {
        Optional <Category> categoryOpt = categoryRepository.findById(categoryId);
        Optional <Product> productOpt = productService.findById(productId);

        if (categoryOpt.isPresent() && productOpt.isPresent()) {
            categoryOpt.get().getProductList().add(productOpt.get());
            productOpt.get().getCategoryList().add(categoryOpt.get());
            categoryRepository.save(categoryOpt.get());
            productService.save(productOpt.get());
        }
    }

    public void removeProductFromCategory(Long categoryId, Long productId) {
        Optional <Category> categoryOpt = categoryRepository.findById(categoryId);
    }

    public void editCategory(Long categoryId, String categoryName, String categoryDescription) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if (categoryOptional.isPresent()) {
            Category existingCategory = categoryOptional.get();
            existingCategory.setName(categoryName);
            existingCategory.setDescription(categoryDescription);
            categoryRepository.save(existingCategory);
        }
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

