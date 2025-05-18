
package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.*;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    @Lazy
    private ProductService productService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private SanitizationService sanitizationService;

    public List<CategoryDTOExtended> getCategories() {
        return toDTOs(categoryRepository.findAll());
    }

    public CategoryDTOExtended getCategory(Long categoryId) {
        Optional <Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return toDTO(category.get());
        }else {
            return null;
        }
    }

    public void createCategory(CategoryDTO categoryDTO) {
        CategoryDTO sanitizedCategoryDto = sanitizationService.sanitizeCategoryDTO(categoryDTO);
        Category category = toCategory(sanitizedCategoryDto);
        this.save(category);
    }

    Category save(Category category) {
        Category sanitizedCategory = sanitizationService.sanitizeCategory(category);
        return categoryRepository.save(sanitizedCategory);
    }

    public void deleteCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()){
            for (Product product : new ArrayList<>(category.get().getProductList())) {
                product.getCategoryList().remove(category.get());
                productService.save(product);
            }

            category.get().getProductList().clear();
            categoryRepository.save(category.get());

            categoryRepository.delete(category.get());
        }

    }

    public void editCategory(Long categoryId, CategoryDTO updatedCategoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        CategoryDTO updatedSanitizedCategoryDto = sanitizationService.sanitizeCategoryDTO(updatedCategoryDTO);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(updatedSanitizedCategoryDto.name());
            category.setDescription(updatedSanitizedCategoryDto.description());
            categoryRepository.save(category);
        }
    }

    public List<ProductDTO> getProductsByCategoryId(Long id) {
        Optional<List<Product>> products = categoryRepository.findProductsByCategoryId(id);
        if (products.isPresent()) {
            productMapper.toDTOs(products.get());
        }
        for (Product product : products.get()) {
            productMapper.toDTO(product);
        }
        return (List<ProductDTO>) productMapper.toProductDTOs(products.get());

    }

    public void addProductToCategory(long categoryId, long productId) {

        Optional <Category> categoryOpt = categoryRepository.findById(categoryId);
        Optional <Product> productOpt = productService.findById(productId);

        if (categoryOpt.isPresent() && productOpt.isPresent()) {

            categoryOpt.get().getProductList().add(productOpt.get());
            productOpt.get().getCategoryList().add(categoryOpt.get());

            categoryRepository.save(categoryOpt.get());
        }
    }

    public void removeProductFromCategory(Long categoryId, Long productId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        Optional<Product> productOpt = productService.findById(productId);

        if (categoryOpt.isPresent() && productOpt.isPresent()) {
            Category category = categoryOpt.get();
            Product product = productOpt.get();

            category.getProductList().remove(product);
            product.getCategoryList().remove(category);

            categoryRepository.save(category);
            productService.save(product);
        }
    }

    private CategoryDTOExtended toDTO(Category category){
        return categoryMapper.toDTO(category);
    }
    private Category toCategory(CategoryDTO categoryDTO){
        return categoryMapper.toCategory(categoryDTO);
    }
    private List<CategoryDTOExtended> toDTOs(List<Category> categories){
        return categoryMapper.toDTOs(categories);
    }
    private List<Category> toCategories(List<CategoryDTO> categoriesDTO){
        return categoryMapper.toCategories(categoriesDTO);
    }


}

