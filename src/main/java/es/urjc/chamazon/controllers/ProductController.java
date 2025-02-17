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
import java.nio.file.StandardCopyOption;
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
    private static final Path IMAGES_FOLDER = Paths.get("images");

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
    public String addProduct(@RequestParam int selectedCategoryId, @RequestParam String selectedCategoryName, Model model) {
    model.addAttribute("product", new Product());
    model.addAttribute("selectedCategoryId", selectedCategoryId);
    model.addAttribute("selectedCategoryName", selectedCategoryName);
    return "addProduct";
    }

    @PostMapping("/products/add")
    public String createProduct(@RequestParam String name,
                                @RequestParam String description,
                                @RequestParam double price,
                                @RequestParam int categoryId,
                                @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return "redirect:/products";
        }
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Files.createDirectories(IMAGES_FOLDER);
            Path imagePath = IMAGES_FOLDER.resolve(fileName);
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            product.setImage(fileName); // Guardamos solo el nombre del archivo
        }

        productService.createProduct(product);
        return "redirect:/categories/" + product.getCategory().getId();
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable int id, @RequestParam String name, 
                          @RequestParam String description, @RequestParam double price,
                          @RequestParam int categoryId, Model model) {
    try {
        Product productDetails = new Product();
        productDetails.setName(name);
        productDetails.setDescription(description);
        productDetails.setPrice(price);
        Category category = categoryService.getCategoryById(categoryId);
        productDetails.setCategory(category);
        
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return "redirect:/categories/" + category.getId();
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}

    @GetMapping("/products/{id}/edit")
    public String updateProducts(Model model, @PathVariable int id) {
    Product product = productService.getProductById(id);
    if (product == null) {
        return "error";
    }
    model.addAttribute("product", product);
    model.addAttribute("categories", categoryService.getAllCategories());
    return "editProduct";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable int id) {
    Product product = productService.getProductById(id);
    if (product != null) {
        int categoryId = product.getCategory().getId(); 
        categoryService.removeProductFromCategory(id, categoryId);
        productService.deleteProduct(id);
        return "redirect:/categories/" + categoryId;
    }
    return "redirect:/products";
    }

    @GetMapping("products/{id}/image")
    public String getProductImage(@PathVariable int id) {
        return productService.getProductImage(id);
    }
}
