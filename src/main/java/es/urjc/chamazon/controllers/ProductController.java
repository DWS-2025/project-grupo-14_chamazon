package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
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
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.List;

import java.sql.Blob;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProductController {
    private static final long NO_CATEGORY_SELECTED = 0;

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
    public String products(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "products_list";
    }

    @GetMapping("/products/{id}")
    public String product(@PathVariable long id, Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product_detail";
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
        return "addProduct";
    }

    @PostMapping("/products/add")
    public String addProduct(Model model, Product product, MultipartFile imageFile) throws IOException {
        /*
         * Optional<Category> category = categoryService.findById(categoryId);
         * if (category == null) {
         * return "redirect:/products";
         * }
         * 
         * product.setCategory(category.get());
         * 
         * // Error control name like 'Empty' is only for demo Fase 1
         * if (imageFile != null && !imageFile.isEmpty() && price > 0 && name != null &&
         * !name.isEmpty() && !name.equals("empty")) {
         * productService.save(new Product(name, price, description,
         * imageFile.getBytes(), 0.0f));
         * imageService.saveImage(imageFile);
         * }else{
         * return "redirect:/products";
         * }
         */
        productService.save(product, imageFile);
        return "redirect:/products";
    }


    @GetMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable long id, Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.findAll());
            return "editProduct";
        } else {
            return "redirect:/products";
        }
    }

    @PostMapping("products/{id}/edit")
    public String updateProduct(@PathVariable long id, Model model, Product newProduct, MultipartFile imageFile) throws IOException{
    //first delete the product i want to emove
    productService.deleteById(id);

    //then save the new product with corresponding id and elements.
    newProduct.setId(id);
    productService.save(newProduct, imageFile);
    return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return "redirect:/products";
        }
        productService.deleteById(id);
        return "redirect:/products";
    }

    /*
     * @PostMapping("/products/{id}/addToCard/{idUser}")
     * public String addToCart(@PathVariable long id, @PathVariable long
     * idUser, @RequestParam long userId) {
     * //Product product = productService.getProduct(id);
     * //if (product != null) {
     * shoppingCarService.addProductToUserShoppingCar(id, idUser);
     * //}
     * return "redirect:/products";
     * }
     */
   

}