package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.ShoppingCar;
import es.urjc.chamazon.services.ShoppingCarService;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static java.lang.Boolean.FALSE;

@Controller
public class ShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private UserService userService;


    @GetMapping("/shoppingCar/History/{idUser}")
    public String shoppingCarHistory (@PathVariable Long idUser, Model model) {
        model.addAttribute("userName", userService.findById(idUser).get().getUserName());
        List<ShoppingCar> listShoppingCar = shoppingCarService.getShoppingCarListByUser(idUser);

        model.addAttribute("listShoppingCar", listShoppingCar);
        //model.addAttribute("productLengthMap", shoppingCarService.getProductsLengthMap(listShoppingCar));

        return "shoppingCarHistory";

    }



    @GetMapping("/shoppingCar/{id}")
    public String shoppingCar (@PathVariable Long id, Model model) {
        model.addAttribute("title", "Shopping Car");
        ShoppingCar sc = shoppingCarService.getShoppingCarById(id);
        if (sc != null) {
            model.addAttribute("shoppingCar", sc);
        }
        List<Product> productList = shoppingCarService.getProductListByIdShoppingCar(id);

        model.addAttribute("idSC", id);
        model.addAttribute("ifNotEnd", sc.getDateSold());
        // model.addAttribute("ifNotProducts", sc.getProducts().isEmpty());
        model.addAttribute("idUser", sc.getUserId());
        model.addAttribute("productList", productList);


        return "shoppingCar";
    }

    @GetMapping("/shoppingCar/endPurchase/{idUser}")
    public String endPurchase (@PathVariable Long idUser,Model model) {
        shoppingCarService.endPurchaseByIdUser(idUser);
        return "redirect:/shoppingCar/History/" + idUser;
    }

    @GetMapping("/shoppingCar/removeProduct/{idProduct}/{idUser}")
    public String removeProduct (@PathVariable Long idProduct, @PathVariable Long idUser, Model model) {

        shoppingCarService.removeProductsFromShoppingCar(idProduct, idUser, FALSE);
        Long idSC = shoppingCarService.getActualShoppingCarByIdUser(idUser).getId();

        return "/shoppingCar/" + idSC;
    }


/*    @PostMapping("/shoppingCar/add")
    public String createShoppingCar (@RequestParam int idUser, @RequestParam ConcurrentMap<Integer, Product> products, Model model) {

        shoppingCarService.addShoppingCarToUser(idUser);

        return "redirect:/shoppingCar";
    }*/


}
