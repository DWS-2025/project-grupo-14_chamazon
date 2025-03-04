package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.*;
import io.github.classgraph.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;


@Controller
public class ProductController {
    private static final int NO_CATEGORY_SELECTED = 0;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/products")
    public String products(Model model, Integer userId) {
        Collection<Product> products = productService.getAllProducts();
        Collection<User> users = userService.getAllUsers();

        model.addAttribute("selectedUserId", userId);
        if (userId != null) {
            User selectedUser = userService.getUser(userId);
            if (selectedUser != null) {
                model.addAttribute("selectedUser", selectedUser);
                List<Product> cartProducts = shoppingCarService.getProductListFromActualShoppingCar(userId);
                model.addAttribute("cartItemCount", cartProducts.size());
            }
        }
        model.addAttribute("productsEachCategory", products);
        model.addAttribute("users", users);
        model.addAttribute("selectedCategoryId", NO_CATEGORY_SELECTED);
        model.addAttribute("selectedCategoryName", "Todas las categorías");
        model.addAttribute("title", "Lista de Productos");
        return "products_list";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
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
        productService.addProduct(name, description, price, category, imageFile.getOriginalFilename());
        imageService.saveImage(imageFile);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/addToCard/{idUser}")
    public String addToCart(@PathVariable int id, @PathVariable int idUser, @RequestParam int userId) {
        //Product product = productService.getProduct(id);
        //if (product != null) {
        shoppingCarService.addProductToUserShoppingCar(id, idUser);
        //}
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable int id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "editProduct";
    }

    @PostMapping("products/{id}/edit")
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

        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imagePath = imageService.saveImage(imageFile);
        }

        productService.updateProduct(id, name, description, price, category, imagePath);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable int id) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }
        productService.delete(id);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable int id) throws MalformedURLException {
        String imageName = productService.findById(id).getImage();
        if (imageName == null) {
            return ResponseEntity.notFound().build();
        }
        return imageService.createResponseFromImage(imageName);
    }

}