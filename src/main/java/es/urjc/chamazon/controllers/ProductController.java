package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.List;

import java.sql.Blob;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/products")
    public String products(Model model, @RequestParam(required = false) Long userId) {
        model.addAttribute("products", productService.findAllProducts());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("selectedUserId", userId);
        return "product/products_list";
    }

    @GetMapping("/products/{id}")
    public String product(@PathVariable long id, Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product/product_detail";
        } else {
            return "redirect:/products";
        }
    }

    @GetMapping("/products/{id}/image")
    public ResponseEntity<Resource> downloadImage(@PathVariable long id) throws SQLException {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent() && product.get().getImageFile() != null) {
            Blob image = product.get().getImageFile();
            Resource file = new InputStreamResource(image.getBinaryStream());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(image.length())
                    .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/products/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "product/addProduct";
    }

    @PostMapping("/products/add")
    public String addProduct(Model model, @ModelAttribute Product product,
            @RequestParam(value = "categoryId", required = false) List<Long> categoryId,
            @RequestParam("imageFileParameter") MultipartFile imageFileParameter) throws IOException {
        // Save the product with the img file to get the id first before saving to
        // category list and then save the product to the category list.

        productService.save(product, imageFileParameter);

        // Get the id of the product to add it to the category list
        Long productId = product.getId();
        if (categoryId != null && !categoryId.isEmpty()) {
            for (Long categoryIdentified : categoryId) {
                categoryService.addProductToCategory(categoryIdentified, productId);
            }
        }

        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable long id, Model model) {
        Optional<Product> optionalProduct = productService.findById(id);
        if (optionalProduct.isPresent()) { // using optional and get product's information
            Product product = optionalProduct.get();
            model.addAttribute("product", product);

            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            return "product/editProduct";
        } else {
            return "redirect:/products";
        }
    }

    @PostMapping("products/{id}/edit")
    public String updateProduct(@PathVariable long id, Model model, @ModelAttribute("product") Product newProduct,
            @RequestParam(value = "categoryId", required = false) List<Long> newCategoryIds,
            @RequestParam("imageFileParameter") MultipartFile imageFileParameter)
            throws IOException {
        // Get existing product at the exact moment by editing
        Optional<Product> existProductActually = productService.findById(id);
        if (!existProductActually.isPresent()) {
            return "redirect:/products";
        }
        Product existProduct = existProductActually.get();
        // then save the new product with corresponding id and elements.
        existProduct.setName(newProduct.getName());
        existProduct.setDescription(newProduct.getDescription());
        existProduct.setPrice(newProduct.getPrice());

        // make sure the image works properly depending on which option did they choose
        if (!imageFileParameter.isEmpty()) {
            productService.save(existProduct, imageFileParameter);
        } else {
            // keep the existing image and just save the updated product
            productService.save(existProduct);
        }

        // remove the product from ALL its current categories
        List<Category> allCategories = categoryService.findAll();
        for (Category category : allCategories) {
            if (category.getProductList().contains(existProduct)) {
                categoryService.removeProductFromCategory(category.getId(), existProduct.getId());
            }
        }

        // add the product to its new selected categories
        if (newCategoryIds != null) {
            for (Long categoryId : newCategoryIds) {
                categoryService.addProductToCategory(categoryId, existProduct.getId());
            }
        }
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return "redirect:/categories";
        }
        productService.deleteById(id);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/addToCard/{idUser}")
    public String addToCart(@PathVariable long id, @PathVariable long idUser) {
        Optional<Product> product = productService.findById(id);
        Optional<User> user = userService.findById(idUser);
        if (product.isPresent() && user.isPresent()) {
            shoppingCarService.addProductToUserShoppingCar(id, idUser);
        }
        return "redirect:/products";
    }

    @GetMapping("products/filter")
    public String filterProducts(Model model,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) Float rating) {

        List<Product> filteredProducts = productService.findByFilters(categoryId, minPrice, maxPrice, rating);

        // Add all necessary attributes to the model
        model.addAttribute("products", filteredProducts);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("users", userService.findAll());
        

        // Preserve filter parameters in the model
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("rating", rating);
        
        return "product/products_list";
    }

}