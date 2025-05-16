package es.urjc.chamazon.restControllers;

import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.ProductDTOExtended;
import es.urjc.chamazon.dto.ProductMapper;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.ProductService;

import java.util.NoSuchElementException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;


import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
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
    @Autowired
    private CategoryService categoryService;

    /*
     * @GetMapping("/{id}")
     * public Page<ProductDTO> getProducts(Pageable pageable) { // same as findall
     * return productService.getProducts(pageable);
     * }
     */

    @GetMapping("/") // same as findAllProducts
    public ResponseEntity<Collection<ProductDTOExtended>> getProducts() { // same as findAllProducts
        Collection <ProductDTOExtended> productsDTOs = productService.getProducts();
        try {
            return ResponseEntity.ok(productsDTOs);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity <ProductDTOExtended> getProduct(@PathVariable long id) { // same as findById
        ProductDTOExtended productDTO = productService.getProduct(id);
        try{
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/") // same as save when the user has added a new product
    public ResponseEntity<ProductDTOExtended> createProduct(@RequestBody ProductDTOExtended productDTO)
    {
        productService.createProduct(productDTO);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(productDTO.id()).toUri();

        return ResponseEntity.created(location).body(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTOExtended> replaceProduct(@PathVariable long id, @RequestBody ProductDTOExtended newProductDTO)   {
        try {
            ProductDTOExtended productDTO = productService.replaceProduct(id, newProductDTO);
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        }catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable long id) {
        try {
            productService.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createProductImage(@PathVariable long id, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        URI location = fromCurrentRequest().build().toUri();
        productService.createProductImage(id, location, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getProductImage(@PathVariable long id) throws SQLException, IOException {
        Resource productImage = productService.getProductImage(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(productImage);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceProductImage(@PathVariable long id, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        productService.replaceProductImage(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deletePostImage(@PathVariable long id) throws IOException {
        productService.deletePostImage(id);
        return ResponseEntity.noContent().build();
    }

    
}