
package es.urjc.chamazon.controllers;

import es.urjc.chamazon.dto.CategoryDTO;
import es.urjc.chamazon.dto.CategoryDTOExtended;
import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.services.CategoryService;

import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;


    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        try{
            model.addAttribute("categories", categoryService.getCategories());
            return "/category/categories";
        }catch (NoSuchElementException e){
            return "/error";
        }

    }


    @GetMapping("/categories/add")
    public String addCategory(Model model) {
        return "/category/addCategory";
    }

    @PostMapping("/categories/add")
    public String addCategory(CategoryDTO newCategoryDTO) {
        categoryService.createCategory(newCategoryDTO);
        return "redirect:/categories";
    }

    @PostMapping ("/categories/delete")
    public String deleteCategory(@RequestParam Long id) {
        try {
            categoryService.deleteCategory(id);
            return "redirect:/categories";
        }catch (NoSuchElementException e) {
            return "/error";
        }
    }

    @GetMapping("/categories/edit")
    public String editCategory(@RequestParam Long id, Model model) {
        try {
            CategoryDTOExtended categoryDTO = categoryService.getCategory(id);
            model.addAttribute("category", categoryDTO);
            return "/category/editCategory";
        } catch (NoSuchElementException e) {
            return "/error";
        }
    }

    @PostMapping("/categories/edit")
    public String editCategory(CategoryDTO updatedCategoryDTO, Model model) {
        try {
            categoryService.editCategory(updatedCategoryDTO.id(), updatedCategoryDTO);
            return "redirect:/categories";
        }catch (NoSuchElementException e) {
            return "/error";
        }
    }

    @GetMapping("/categories/products")
    public String getProductsByCategory(@RequestParam Long id, Model model) {
        try{
            model.addAttribute("category", categoryService.getCategory(id));
            model.addAttribute("categories", categoryService.getCategories());
            model.addAttribute("products", categoryService.getProductsByCategoryId(id));
            model.addAttribute("users", userService.getAllUsers());
            return "product/products_list";
        } catch (NoSuchElementException e) {
            return "/error";
        }
    }
}
