package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.ShopingCar;
import es.urjc.chamazon.services.ShopingCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ShopingCarController {

    @Autowired
    private ShopingCarService shopingCarService;


    @GetMapping("/shopingCar")
    public String shopingCar (Model model) {
        model.addAttribute("title", "Shoping Car");
        model.addAttribute("listShopingCar", shopingCarService.getShopingCars());
        return "shopingCar";
    }

    @PostMapping("/shopingCar/add")
    public String createShopingCar (@RequestParam int idUser, @RequestParam List<Object> products, Model model) {

        ShopingCar shopingCar = new ShopingCar(idUser, products);
        shopingCarService.addShopingCar(shopingCar);

        return "redirect:/shopingCar";
    }

}
