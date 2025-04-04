package es.urjc.chamazon.controllers;

import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.ProductMapper;
import es.urjc.chamazon.dto.CategoryDTO;
import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
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
        model.addAttribute("products", productService.getProducts());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("selectedUserId", userId);
        return "product/products_list";
    }

    @GetMapping("/products/{id}")
    public String product(@PathVariable long id, Model model) {
        ProductDTO productDTO = productService.getProduct(id); // findById with DTO
        if (productDTO != null) {
            model.addAttribute("product", productDTO);
            return "product/product_detail";
        } else {
            return "redirect:/products";
        }
    }

    @GetMapping("/products/{id}/image")
    public ResponseEntity<Resource> downloadImage(@PathVariable long id) throws SQLException {
        Optional<Product> product = productService.findById(id); // findById with entity
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
        model.addAttribute("categories", categoryService.getCategories());
        return "product/addProduct";
    }

    @PostMapping("/products/add")
    public String addProduct(Model model, @ModelAttribute ProductDTO productDTO,
                             @RequestParam(value = "categoryId", required = false) List<Long> categoryId,
                             @RequestParam("imageFileParameter") MultipartFile imageFileParameter) throws IOException {
        // Save the product with the img file to get the id first before saving to
        // category list and then save the product to the category list.

        //productService.save(product, imageFileParameter);

        ProductDTO savedProduct = productService.save(productDTO, imageFileParameter);

        // Get the id of the product to add it to the category list
        Long productId = savedProduct.id();
        if (categoryId != null && !categoryId.isEmpty()) {
            for (Long categoryIdentified : categoryId) {
                //categoryService.addProductToCategory(categoryIdentified, productId);
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

            List<CategoryDTO> categories = categoryService.getCategories();
            model.addAttribute("categories", categories);
            return "product/editProduct";
        } else {
            return "redirect:/products";
        }
    }

    @PostMapping("products/{id}/edit")
    public String updateProduct(@PathVariable long id, Model model, @ModelAttribute("product") Product newProduct,
                                @RequestParam(value = "categoryId", required = false) List<Long> newCategoryIds,
                                @RequestParam(value = "imageFileParameter", required = false) MultipartFile imageFileParameter)
            throws IOException {
        // Get existing product at the exact moment by editing
        //Optional<ProductDTO> existProductActually = productService.findById(id);
        Optional<Product> existProductActually = productService.findById(id);
        if (!existProductActually.isPresent()) {
            return "redirect:/products";
        }

        /*Meterlo en el service el proceso logico
        Product prs = existProductActually.get();

        prs.setName(newProduct.getName());
        prs.setPrice(newProduct.getPrice());
        prs.setDescription(newProduct.getDescription());

        productService.save(prs, newProduct.getImageFile());
        */
        //Create DTO
        ProductDTO productDTO = new ProductDTO(newProduct.getId(),
                newProduct.getName(), newProduct.getPrice(),
                newProduct.getDescription(), newProduct.getRating(), existProductActually.get().getCategoryList(), existProductActually.get().getShoppingCarList());

        // make sure the image works properly depending on which option did they choose

        productService.save(id, productDTO, imageFileParameter);

        // remove the product from ALL its current categories
        List<CategoryDTO> allCategories = categoryService.getCategories();
        for (CategoryDTO CategoryDTO : allCategories) {
            /*if (CategoryDTO.getProductList().contains(existProduct)) {
                categoryService.removeProductFromCategory(category.getId(), existProduct.getId());
            }*/
        }

        // add the product to its new selected categories
        if (newCategoryIds != null) {
            for (Long categoryId : newCategoryIds) {
                //categoryService.addProductToCategory(categoryId, existProduct.getId());
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
        /*UserDTO user = userService.getUser(idUser);
        if (product.isPresent() && user.isPresent()) {
            shoppingCarService.addProductToUserShoppingCar(id, idUser);
        }*/
        return "redirect:/products";
    }

    @GetMapping("products/filter")
    public String filterProducts(Model model,
                                 @RequestParam(required = false) Long categoryId,
                                 @RequestParam(required = false) Float minPrice,
                                 @RequestParam(required = false) Float maxPrice,
                                 @RequestParam(required = false) Float rating,
                                 @RequestParam(required = false) Long userId) {

        List<Product> filteredProducts = productService.findByFilters(categoryId, minPrice, maxPrice, rating);

        // Verify filters
        boolean hasFilters = categoryId != null || minPrice != null || maxPrice != null || rating != null;

        if (hasFilters) {
            // If filters are applied, show filtered products
            filteredProducts = productService.findByFilters(categoryId, minPrice, maxPrice, rating);
        } else {
            // If no filters are applied, show all products
            productService.getProducts();
        }

        // Add all necessary attributes to the model
        //List<Category> allCategories = categoryService.getCategories();
        model.addAttribute("products", filteredProducts);
        //model.addAttribute("categories", allCategories);
        model.addAttribute("users", userService.getAllUsers());

        // Preserve filter parameters in the model
        model.addAttribute("categoryId", categoryId != null ? categoryId.toString() : "");
        model.addAttribute("minPrice", minPrice != null ? minPrice.toString() : "");
        model.addAttribute("maxPrice", maxPrice != null ? maxPrice.toString() : "");
        model.addAttribute("rating", rating != null ? rating.toString() : "");
        model.addAttribute("selectedUserId", userId);

        return "product/products_list";
    }

}