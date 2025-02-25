package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.Collection;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        Collection<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories";

    }
    @GetMapping("/categories/add")
    public String addCategory(Model model) {
        return "addCategory";
    }

    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String categoryName) {
        categoryService.addCategory(categoryName);
        return "redirect:/categories";
    }

    /*@PostMapping("/categories/delete")
    public String deleteCategory(Model model,@RequestParam int categoryId){
        Category category = categoryService.getCategoryById(categoryId);
        if (category != null) {
            categoryService.deleteCategory(categoryId);
        } else {
            return "error";
        }
        return "redirect:/categories";
    }*/

    @PostMapping("/categories/delete")
    public String deleteCategory(@RequestParam int categoryId) {
        categoryService.deleteCategory(categoryId);
        return "redirect:/categories";
    }
    @GetMapping("/categories/delete")
    public String deleteCategory(@RequestParam int categoryId, Model model) {
        categoryService.deleteCategory(categoryId);
        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}")
    public String showProductsEachCategory(Model model, @PathVariable int id) {
    Collection<Product> productsEachCategory = categoryService.getProductsFromCategory(id);
    Category category = categoryService.getCategoryById(id);
    model.addAttribute("productsEachCategory", productsEachCategory);
    model.addAttribute("selectedCategoryId", id);
    model.addAttribute("selectedCategoryName", category.getName());
    model.addAttribute("title", "Lista de Productos");
    return "products_list";
    }

    @GetMapping("/categories/edit")
    public String editCategory(@RequestParam int categoryId, Model model) {
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        return "editCategory";
    }

    @PostMapping("/categories/edit")
    public String editCategory(@RequestParam int categoryId, @RequestParam String categoryName, Model model) {
        Category category = categoryService.getCategoryById(categoryId);
        category.setName(categoryName);
        categoryService.updateCategory(category);
        return "redirect:/categories";
    }


    @GetMapping("/")
    public String home() {
        return "main";
    }
}
