package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.ShoppingCar;
import es.urjc.chamazon.services.ShoppingCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ConcurrentMap;

@Controller
public class ShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;


    @GetMapping("/shoppingCar")
    public String shoppingCar (Model model) {
        model.addAttribute("title", "Shopping Car");
        //model.addAttribute("listShoppingCar", shoppingCarService.getShoppingCars());
        return "shoppingCar";
    }

    @PostMapping("/shoppingCar/add")
    public String createShoppingCar (@RequestParam int idUser, @RequestParam ConcurrentMap<Integer, Product> products, Model model) {

        shoppingCarService.addShoppingCarToUser(idUser);

        return "redirect:/shoppingCar";
    }

}
