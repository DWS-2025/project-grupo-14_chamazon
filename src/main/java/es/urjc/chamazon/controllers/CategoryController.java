package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model; // Importación correcta
import java.util.Collection;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        Collection<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "main";

    }
    // Añadir una nueva categoría
    @PostMapping("/categories/add")
    public String addCategory(@RequestParam int id, @RequestParam String name) {
        Category category = new Category(id, name);
        categoryService.addCategory(category);
        return "redirect:/categories"; // Redirigir una vez que la categoría se añade
    }
    @GetMapping("/")
    public String home() {
        return "redirect:/categories"; // Redirige a categorías o a otra página
    }
}
