package es.urjc.chamazon.restControllers;

import es.urjc.chamazon.dto.*;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.*;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductMapper productMapper;

    // === GET ALL PRODUCTS ===
    @GetMapping("")
    public ResponseEntity<Collection<ProductDTOExtended>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    // === GET SINGLE PRODUCT ===
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTOExtended> getProduct(@PathVariable long id) {
        try {
            return ResponseEntity.ok(productService.getProduct(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === CREATE PRODUCT ===
    @PostMapping("")
    public ResponseEntity<ProductDTOExtended> createProduct(@RequestBody ProductDTOExtended productDTO) {
        ProductDTOExtended savedProduct = productService.createProduct(productDTO);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(savedProduct.id()).toUri();
        return ResponseEntity.created(location).body(savedProduct);
    }

    // === UPDATE PRODUCT ===
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTOExtended> replaceProduct(@PathVariable long id, @RequestBody ProductDTOExtended newProductDTO) {
        try {
            ProductDTOExtended productDTO = productService.replaceProduct(id, newProductDTO);
            return ResponseEntity.ok(productDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === DELETE PRODUCT ===
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        try {
            commentService.deleteCommentByProductId(id);
            productService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === UPLOAD PRODUCT IMAGE ===
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createProductImage(@PathVariable long id, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        URI location = fromCurrentRequest().build().toUri();
        productService.createProductImage(id, location, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.created(location).build();
    }

    // === GET PRODUCT IMAGE ===
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getProductImage(@PathVariable long id) throws SQLException, IOException {
        Resource productImage = productService.getProductImage(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(productImage);
    }

    // === REPLACE PRODUCT IMAGE ===
    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceProductImage(@PathVariable long id, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        productService.replaceProductImage(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.noContent().build();
    }

    // === DELETE PRODUCT IMAGE ===
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteProductImage(@PathVariable long id) throws IOException {
        productService.deletePostImage(id);
        return ResponseEntity.noContent().build();
    }

    // === FILTER PRODUCTS ===
    @PostMapping("/filter")
    public ResponseEntity<List<SimpleProductDTO>> filterProducts(
            @RequestBody FilteredDto filteredDto) {
        List<Product> filtered = productService.findByFilters(
                filteredDto.categoryId(),
                filteredDto.minPrice(),
                filteredDto.maxPrice(),
                filteredDto.rating()
        );
        List<SimpleProductDTO> filteredDTOs = filtered.stream()
                .map(productMapper::toSimpleDTO)
                .toList();
        return ResponseEntity.ok(filteredDTOs);
    }

    /* === ADD TO CART ===
    @PostMapping("/{productId}/add-to-cart/{userId}")
    public ResponseEntity<Void> addToCart(@PathVariable long productId, @PathVariable long userId) {
        Optional<Product> product = productService.findById(productId);
        if (product.isPresent()) {
            shoppingCarService.addProductToUserShoppingCar(productId, userId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }*/

    // === ATTACH FILE TO PRODUCT ===
    @PostMapping("/{id}/file")
    public ResponseEntity<String> attachFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileStorageService.storeFile(file, id);
            return ResponseEntity.ok("Archivo subido: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir archivo: " + e.getMessage());
        }
    }

    // === DOWNLOAD FILE ATTACHED TO PRODUCT ===
    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        Resource resource = fileStorageService.loadFileAsResource(id);
        String originalFilename = fileStorageService.getOriginalFilename(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFilename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
