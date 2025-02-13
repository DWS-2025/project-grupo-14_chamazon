package es.urjc.chamazon.controllers;
import es.urjc.chamazon.models.Category;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    private List<Product> products = new ArrayList<>();
    @Autowired
    private CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
        products.add(new Product(1, "Producto 1", "Descripción del producto 1", 10.0, null, new Category("Electronica")));
        products.add(new Product(2, "Producto 2", "Descripción del producto 2", 20.0, null, new Category("Ropa")));
        products.add(new Product(3, "Producto 3", "Descripción del producto 3", 30.0, null, new Category("Ropa")));
        products.addAll(productService.getAllProducts());
    }

    @GetMapping("/products")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
//        model.addAttribute("categories", categories);
        return "products_list";
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "product_detail";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "addProduct";
    }

    @PostMapping("/products/add")
    public String createProduct(@RequestParam String name,
                                @RequestParam String description,
                                @RequestParam double price,
                                @RequestParam String categoryName,
                                @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        Category category = categoryService.getCategoryByName(categoryName);
        if (category == null) {
            return "redirect:/products";
        }
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            Files.createDirectories(IMAGES_FOLDER);
            Path imagePath = IMAGES_FOLDER.resolve("product_" + System.currentTimeMillis() + ".jpg");
            imageFile.transferTo(imagePath);
            product.setImage(Files.readAllBytes(imagePath));
        }

        productService.createProduct(product);
        return "redirect:/products";
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product productDetails) {
        return productService.updateProduct(id, productDetails);
    }

    @DeleteMapping("/products/{id}")
    public boolean deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }
    @GetMapping("products/{id}/image")
    public byte[] getProductImage(@PathVariable int id) {
        return productService.getProductImage(id);
    }
}
