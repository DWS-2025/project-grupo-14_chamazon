package es.urjc.chamazon.restControllers;


import es.urjc.chamazon.dto.CategoryDTO;
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

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categoriesDTOs = categoryService.getCategories();
        try {
            return ResponseEntity.ok(categoriesDTOs);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable long id) {
        CategoryDTO categoryDTO = categoryService.getCategory(id);
        try {
            return ResponseEntity.ok(categoryDTO);
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.createCategory(categoryDTO);
        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(categoryDTO.id()).toUri();
        return ResponseEntity.created(location).body(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(categoryService.getCategory(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable long id, @RequestBody CategoryDTO categoryDTO) {
        categoryService.editCategory(id, categoryDTO);
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<CategoryDTO>> getProductsFromCategory(@PathVariable long id) {
        categoryService.getProductsByCategoryId(id);
        return ResponseEntity.ok().build();
    }

}
