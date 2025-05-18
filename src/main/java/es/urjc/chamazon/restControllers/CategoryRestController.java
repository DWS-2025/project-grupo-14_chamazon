package es.urjc.chamazon.restControllers;

import es.urjc.chamazon.dto.CategoryDTO;
import es.urjc.chamazon.dto.CategoryDTOExtended;
import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.UserService;
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
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    // GET /api/categories/
    @GetMapping("/")
    public ResponseEntity<List<CategoryDTOExtended>> getAllCategories() {
        try {
            List<CategoryDTOExtended> categories = categoryService.getCategories();
            return ResponseEntity.ok(categories);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /api/categories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTOExtended> getCategoryById(@PathVariable long id) {
        try {
            CategoryDTOExtended category = categoryService.getCategory(id);
            return ResponseEntity.ok(category);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/categories/
    @PostMapping("/")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            categoryService.createCategory(categoryDTO);
            URI location = fromCurrentRequest().path("/{id}")
                    .buildAndExpand(categoryDTO.id()).toUri();
            return ResponseEntity.created(location).body(categoryDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE /api/categories/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT /api/categories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTOExtended> updateCategory(@PathVariable long id, @RequestBody CategoryDTO categoryDTO) {
        try {
            categoryService.editCategory(id, categoryDTO);
            CategoryDTOExtended updated = categoryService.getCategory(id);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /api/categories/{id}/products
    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsFromCategory(@PathVariable long id) {
        try {
            List<ProductDTO> products = categoryService.getProductsByCategoryId(id);
            return ResponseEntity.ok(products);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
