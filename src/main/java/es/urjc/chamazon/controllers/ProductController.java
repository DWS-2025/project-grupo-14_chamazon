package es.urjc.chamazon.controllers;

import es.urjc.chamazon.configurations.SecurityUtils;
import es.urjc.chamazon.dto.*;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

import java.sql.Blob;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

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
        if (!SecurityUtils.isAdmin()){
            userId = (Long) model.getAttribute("idUser");
        }
        model.addAttribute("selectedUserId", userId);
        return "product/products_list";
    }

    @GetMapping("/products/{id}")
    public String product(@PathVariable long id, Model model) {

        ProductDTOExtended productDTO = productService.getProduct(id);
        List<CategoryDTO> categories = productDTO.categoryDTOList();

        // Verificación mejorada del archivo
        boolean hasFile = false;
        String originalFilename = null;

        try {
            originalFilename = fileStorageService.getOriginalFilename(id);
            Path filePath = Paths.get(fileStorageService.getFileStorageLocation().toString(),
                    id + "_" + originalFilename);
            if (Files.exists(filePath)) {
                hasFile = true;
                model.addAttribute("fileName", originalFilename);
            }
        } catch (Exception e) {
            // No hacer nada, hasFile sigue siendo false
        }

        boolean isAuthenticated = SecurityUtils.isAuthenticated();
        boolean isAdmin = SecurityUtils.isAdmin();
        String currentUsername = SecurityUtils.getCurrentUsername();

        List<CommentDTO> commentDTOs = commentService.findByProductId(id).stream().map(comment -> {
            boolean isOwner = isAuthenticated && comment.getUser().userName().equals(currentUsername);
            boolean canEditOrDelete = isAdmin || isOwner;
            return new CommentDTO(
                    comment.getId(),
                    comment.getComment(),
                    comment.getRating(),
                    comment.getUser(),
                    comment.getProduct(),
                    canEditOrDelete
            );
        }).collect(Collectors.toList());

        boolean hasDescription = (productDTO.description() != null && !productDTO.description().trim().isEmpty());
        model.addAttribute("hasDescription", hasDescription);
        model.addAttribute("product", productDTO);
        model.addAttribute("categories", categories);
        model.addAttribute("comments", commentDTOs);
        model.addAttribute("hasFile", hasFile);
        return "product/product_detail";
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
        List<CategoryDTOExtended> allCategories = categoryService.getCategories();
        model.addAttribute("categories", allCategories);
        return "product/addProduct";
    }

    @PostMapping("/products/add")
    public String addProduct(Model model, @ModelAttribute ProductDTOExtended productDTO,
                             @RequestParam(value = "categoryId", required = false) List<Long> categoryId,
                             @RequestParam("imageFileParameter") MultipartFile imageFileParameter, @RequestParam(value = "fileParameter", required = false) MultipartFile file) throws IOException {
        // Save the product with the img file to get the id first before saving to
        // category list and then save the product to the category list.
        ProductDTO savedProduct = productService.save(productDTO, imageFileParameter);

        // Get the id of the product to add it to the category list
        Long productId = savedProduct.id();

        if (file != null && !file.isEmpty()) {
            fileStorageService.storeFile(file, productId);
        }

        if (categoryId == null || categoryId.isEmpty()){
            // If no categories are selected, redirect to the product list
            return "redirect:/products";
        }
        else if (categoryId != null && !categoryId.isEmpty()) {
            for (Long categoryIdentified : categoryId) {
                categoryService.addProductToCategory(categoryIdentified, productId);
            }
        }

        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable long id, Model model) {
        Optional<Product> optionalProduct = productService.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            List<CategoryDTOExtended> allCategories = categoryService.getCategories();

            Set<Long> selectedCategoryIds = product.getCategoryList().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet());

            List<Map<String, Object>> categoryMapList = allCategories.stream().map(cat -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", cat.id());
                map.put("name", cat.name());
                map.put("selected", selectedCategoryIds.contains(cat.id()));
                return map;
            }).toList();

            model.addAttribute("categories", categoryMapList);
            model.addAttribute("product", product);
            return "product/editProduct";
        } else {
            return "redirect:/products";
        }
    }

    @PostMapping("products/{id}/edit")
    public String updateProduct(@PathVariable long id, Model model, @ModelAttribute("product") Product newProduct,
                                @RequestParam(value = "categoryId", required = false) List<Long> newCategoryIds,
                                @RequestParam(value = "imageFileParameter", required = false) MultipartFile imageFileParameter)
            throws IOException, SQLException {
        // Get existing product at the exact moment by editing
        //Optional<ProductDTO> existProductActually = productService.findById(id);
        Optional<Product> existProductActually = productService.findById(id);

        if (!existProductActually.isPresent()) {
            return "redirect:/products";
        }

        productService.update(existProductActually.get(), newProduct, imageFileParameter);

        // remove the product from ALL its current categories
        List<Category> categories = new ArrayList<>(existProductActually.get().getCategoryList());
        for (Category category : categories) {
            categoryService.removeProductFromCategory(category.getId(), id);
        }


        // add the product to its new selected categories
        if (newCategoryIds != null) {
            for (Long categoryId : newCategoryIds) {
                categoryService.addProductToCategory(categoryId,id);
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
        commentService.deleteCommentByProductId(id);
        productService.deleteById(id);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/addToCard/{idUser}")
    public String addToCart(@PathVariable long id, @PathVariable long idUser) {
        Optional<Product> product = productService.findById(id);
        UserDTO user = userService.getUser(idUser);
        if (product.isPresent() && user != null) {
            shoppingCarService.addProductToUserShoppingCar(id, idUser);
        }
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

        // Add all necessary attributes to the model
        List<CategoryDTOExtended> allCategories = categoryService.getCategories();
        model.addAttribute("products", filteredProducts);
        model.addAttribute("categories", allCategories);
        model.addAttribute("users", userService.getAllUsers());

       // boolean hasFilters = categoryId != null || minPrice != null || maxPrice != null || rating != null;
        //model.addAttribute("hasNoResults", filteredProducts.isEmpty() && hasFilters);   


        // Preserve filter parameters in the model
        model.addAttribute("categoryId", categoryId != null ? categoryId.toString() : "");
        model.addAttribute("minPrice", minPrice != null ? minPrice.toString() : "");
        model.addAttribute("maxPrice", maxPrice != null ? maxPrice.toString() : "");
        model.addAttribute("rating", rating != null ? rating.toString() : "");
        if (!SecurityUtils.isAdmin()){
            userId = (Long) model.getAttribute("idUser");
        }
        model.addAttribute("selectedUserId", userId);

        return "product/products_list";
    }

    @PostMapping("/products/{id}/attach-file")
    public String attachFile(@PathVariable Long id,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        try {
            String fileName = fileStorageService.storeFile(file, id);
            redirectAttributes.addFlashAttribute("message",
                    "Archivo subido correctamente: " + fileName.substring(fileName.indexOf('_') + 1));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al subir archivo: " + e.getMessage());
        }
        return "redirect:/products/" + id;
    }

    @GetMapping("/products/{id}/download-file")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        Resource resource = (Resource) fileStorageService.loadFileAsResource(id);
        String originalFilename = fileStorageService.getOriginalFilename(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + originalFilename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


}