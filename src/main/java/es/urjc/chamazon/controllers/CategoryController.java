
package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.services.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/")
    public String home() {
        return "main";
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "/category/categories";

    }

    @GetMapping("/categories/add")
    public String addCategory(Model model) {
        return "/category/addCategory";
    }

    @PostMapping("/categories/add")
    public String addCategory(Category newCategory) {
        categoryService.save(newCategory);
        return "redirect:/categories";
    }

    @PostMapping ("/categories/delete")
    public String deleteCategory(@RequestParam long categoryId) {
        Optional<Category> category = categoryService.findById(categoryId);
        if (category.isPresent()) {
            categoryService.deleteById(categoryId);
            return "redirect:/categories";
        }else{
            return "/error/error";
        }
    }

    @GetMapping("/categories/edit")
    public String editCategory(@RequestParam Long categoryId, Model model) {
        Optional <Category> category = categoryService.findById(categoryId);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
        }else {
            return "/error/error";
        }
        return "/category/editCategory";
    }

    @PostMapping("/categories/edit")
    public String editCategory(@RequestParam Long categoryId, @RequestParam String categoryName, @RequestParam String categoryDescription, Model model) {
        Optional <Category> category = categoryService.findById(categoryId);
        if (category.isPresent()) {
            categoryService.editCategory(categoryId, categoryName, categoryDescription);
            return "redirect:/categories";
        }else {
            return "/error/error";
        }
    }

    @GetMapping("/categories/products")
    public String getProductsByCategory(@RequestParam Long categoryId, Model model) {
        Optional<Category> category = categoryService.findById(categoryId);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            model.addAttribute("products", categoryService.getProductsByCategoryId(categoryId));
            return "product/products_list";
        } else {
            return "error/error";
        }
    }
}
