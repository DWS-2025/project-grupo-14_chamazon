package es.urjc.chamazon.restControllers;

import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.ProductMapper;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/")
    public Collection<ProductDTO> getProducts() { // same as findall
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable long id) { // same as findById
        ProductDTO product = productService.getProduct(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/") // same as save when the user has added a new product
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO,
            @RequestParam MultipartFile imageFile) {
        productService.save(productDTO, imageFile);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(productDTO.id()).toUri();

        return ResponseEntity.created(location).body(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable long id) {
        ProductDTO product = productService.getProduct(id);

        if (product != null) {
            productService.deleteById(id);
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long id, @RequestBody ProductDTO newProductDTO,
            @RequestParam(required = false) MultipartFile imageFile) {
        Optional<Product> existingProductOptional = productService.findById(id);
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            // convert DTO to entity making sure it uses the service update method
            Product newProduct = productMapper.toProduct(newProductDTO);
            try {
                ProductDTO updatedProductDTO = productService.update(existingProduct, newProduct, null);
                return ResponseEntity.ok(updatedProductDTO);
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile) {
        Optional<Product> existingProductOptional = productService.findById(id);
        if (existingProductOptional.isPresent()) {
            try {
                URI location = fromCurrentRequest().build().toUri();

                Product existingProduct = existingProductOptional.get();

                ProductDTO updatedProductDTO = productService.update(existingProduct, null, imageFile);

                return ResponseEntity.created(location).build();
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}