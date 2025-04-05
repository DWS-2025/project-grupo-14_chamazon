package es.urjc.chamazon.restControllers;


import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.ProductMapper;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductMapper productMapper;

    @GetMapping("/products/")
    public Collection<ProductDTO> getProducts() { // same as findall
        return productService.getProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable long id) { // same as findById
        ProductDTO product = productService.getProduct(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/products/") // same as save when the user has added a new product
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO, @RequestParam MultipartFile imageFile) {
        productService.save(productDTO, imageFile);

        URI location = fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productDTO.id())
                .toUri();

        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable long id) {
        ProductDTO product = productService.getProduct(id);

        if (product != null) {
            productService.deleteById(id);
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long id, @RequestBody ProductDTO newProductDTO) {
        Optional<Product> existingProduct = productService.findById(id);
        if (existingProduct.isPresent()) {
            newProductDTO.setId(id);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
