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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/products")
    public String getAllProducts(Model model) {
        Collection<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
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
            String imagePath = String.valueOf(IMAGES_FOLDER.resolve(product.getName() + System.currentTimeMillis() + ".jpg"));
            imageFile.transferTo(new File(imagePath));
            product.setImage(imagePath);
            //product.setImage(Files.readAllBytes(imagePath));
        }

        productService.createProduct(product);
        return "redirect:/categories/" + product.getCategory().getId();
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable int id, @RequestBody Product productDetails, Model model) {
        Product product = productService.updateProduct(id, productDetails);
        model.addAttribute("product", product);
        return "redirect:/categories/" + product.getCategory().getId();
    }

    @GetMapping("/products/{id}/edit")
    public String updateProducts(Model model, @PathVariable int id) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "editProduct";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable int id) {

        if (productService.getProductById(id) != null) {
            categoryService.removeProductFromCategory(id, categoryService.getCategoryIdByProductId(id));
            productService.deleteProduct(id);
            return "redirect:/categories/" + categoryService.getCategoryIdByProductId(id);
        }
        return "redirect:/products";
    }

    @GetMapping("products/{id}/image")
    public String getProductImage(@PathVariable int id) {
        return productService.getProductImage(id);
    }
}
