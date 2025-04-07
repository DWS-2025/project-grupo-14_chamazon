package es.urjc.chamazon.restControllers;


import es.urjc.chamazon.dto.CategoryDTO;
import es.urjc.chamazon.dto.CategoryDTOExtended;
import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("api/categories")
public class CategoryRestController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<List<CategoryDTOExtended>> getAllCategories() {
        List<CategoryDTOExtended> categoriesDTOs = categoryService.getCategories();
        try {
            return ResponseEntity.ok(categoriesDTOs);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTOExtended> getCategoryById(@PathVariable long id) {
        CategoryDTOExtended categoryDTO = categoryService.getCategory(id);
        try {
            return ResponseEntity.ok(categoryDTO);
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.createCategory(categoryDTO);
        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(categoryDTO.id()).toUri();
        return ResponseEntity.created(location).body(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTOExtended> deleteCategory(@PathVariable long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(categoryService.getCategory(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTOExtended> updateCategory(@PathVariable long id, @RequestBody CategoryDTO categoryDTO) {
        categoryService.editCategory(id, categoryDTO);
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsFromCategory(@PathVariable long id) {
        List<ProductDTO> categoriesDTOs = categoryService.getProductsByCategoryId(id);
        return ResponseEntity.ok(categoriesDTOs);
    }

}
