
package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.CategoryDTO;
import es.urjc.chamazon.dto.CategoryMapper;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryMapper categoryMapper;


    public List<CategoryDTO> getCategories() {
        return toDTOs(categoryRepository.findAll());
    }

    public CategoryDTO getCategory(Long categoryId) {
        Optional <Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return toDTO(category.get());
        }else {
            return null;
        }
    }

    public void createCategory(CategoryDTO categoryDTO) {
        Category category = toCategory(categoryDTO);
        this.save(category);
    }
    void save(Category category) {
        categoryRepository.save(category);
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

        if (categoryOptional.isPresent()) {

            Category category = categoryOptional.get();
            category.setName(updatedCategoryDTO.name());
            category.setDescription(updatedCategoryDTO.description());
            categoryRepository.save(category);
        }
    }

    public Object getProductsByCategoryId(Long id) {
        return categoryRepository.findProductsByCategoryId(id);
    }

    /*public void addProductToCategory(CategoryDTO categoryDTO, ProductDTO productDTO) {

        Category category = toCategory(categoryDTO);

        Optional <Category> categoryOpt = categoryRepository.findById(category.getId());
        Optional <Product> productOpt = productService.findById(productId);

        if (categoryOpt.isPresent() && productOpt.isPresent()) {

            categoryOpt.get().getProductList().add(productOpt.get());
            productOpt.get().getCategoryList().add(categoryOpt.get());
            categoryRepository.save(categoryOpt.get());
        }
    }*/

    public void removeProductFromCategory(Long categoryId, Long productId) {

        Optional <Category> categoryOpt = categoryRepository.findById(categoryId);
        Optional<Product> productOpt = productService.findById(productId);

        if (categoryOpt.isPresent() && productOpt.isPresent()) {
            Category category = categoryOpt.get();
            Product product = productOpt.get();
            category.getProductList().remove(product);
            product.getCategoryList().remove(category);
            categoryRepository.save(category);
        }
    }

    private CategoryDTO toDTO(Category category){
        return categoryMapper.toDTO(category);
    }
    private Category toCategory(CategoryDTO categoryDTO){
        return categoryMapper.toCategory(categoryDTO);
    }
    private List<CategoryDTO> toDTOs(List<Category> categories){
        return categoryMapper.toDTOs(categories);
    }
    private List<Category> toCategories(List<CategoryDTO> categoriesDTO){
        return categoryMapper.toCategories(categoriesDTO);
    }
}

