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
import java.nio.file.StandardCopyOption;
import java.util.Collection;

@Controller
public class ProductController {
    private static final Path IMAGES_FOLDER = Paths.get("images");

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model) {
        Collection<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products_list";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model, 
                           @RequestParam int selectedCategoryId, 
                           @RequestParam String selectedCategoryName) {
        model.addAttribute("product", new Product());
        model.addAttribute("selectedCategoryId", selectedCategoryId);
        model.addAttribute("selectedCategoryName", selectedCategoryName);
        return "addProduct";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam String name,
                           @RequestParam String description,
                           @RequestParam double price,
                           @RequestParam int categoryId,
                           @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return "redirect:/products";
        }

        String imageName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageName = productService.processImage(imageFile);
        }

        productService.addProduct(name, description, price, category, imageName);
        return "redirect:/categories/" + category.getId();
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable int id,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam double price,
                              @RequestParam int categoryId,
                              @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return "redirect:/products";
        }

        String imageName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageName = productService.processImage(imageFile);
        }

        productService.updateProduct(id, name, description, price, category, imageName);
        return "redirect:/categories/" + category.getId();
    }
}