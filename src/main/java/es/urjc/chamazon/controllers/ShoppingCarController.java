/*
package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.ShoppingCar;
import es.urjc.chamazon.repositories.ShoppingCarRepository;
import es.urjc.chamazon.services.ShoppingCarService;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.FALSE;

@Controller
public class ShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;
    @Autowired
    private ShoppingCarRepository shoppingCarRepository;
    @Autowired
    private UserService userService;


    @GetMapping("/shoppingCar/History/{idUser}")
    public String shoppingCarHistory (@PathVariable int idUser, Model model) {
        model.addAttribute("userName", userService.getUserById(idUser).getUserName());
        List<ShoppingCar> listShoppingCar = shoppingCarService.getShoppingCarListByUser(idUser);

        model.addAttribute("listShoppingCar", listShoppingCar);
        model.addAttribute("productLengthMap", shoppingCarService.getProductsLengthMap(listShoppingCar));

        return "shoppingCarHistory";

    }



    @GetMapping("/shoppingCar/{id}")
    public String shoppingCar (@PathVariable Long id, Model model) {
        model.addAttribute("title", "Shopping Car");
        Optional<ShoppingCar> sc = shoppingCarRepository.findById(id);
        if (sc.isPresent()) {
            model.addAttribute("shoppingCar", sc.get());
        }
        List<Product> productList = shoppingCarService.getProductListByIdShoppingCar(id);

        model.addAttribute("idSC", id);
        model.addAttribute("ifNotEnd", sc.getDateSold());
        model.addAttribute("ifNotProducts", sc.getProducts().isEmpty());
        model.addAttribute("idUser", sc.getIdUser());
        model.addAttribute("productList", productList);


        return "shoppingCar";
    }

    @GetMapping("/shoppingCar/endPurchase/{idUser}")
    public String endPurchase (@PathVariable int idUser,Model model) {
        shoppingCarService.endPurchaseByIdUser(idUser);
        return "redirect:/shoppingCar/History/" + idUser;
    }

    @GetMapping("/shoppingCar/removeProduct/{idProduct}/{idUser}")
    public String removeProduct (@PathVariable int idProduct, @PathVariable int idUser, Model model) {

        shoppingCarService.removeProductsFromShoppingCar(idProduct, idUser, FALSE);
        Integer idSC = shoppingCarService.getActualShoppingCarByIdUser(idUser).getId();

        return "/shoppingCar/" + idSC;
    }


*/
/*    @PostMapping("/shoppingCar/add")
    public String createShoppingCar (@RequestParam int idUser, @RequestParam ConcurrentMap<Integer, Product> products, Model model) {

        shoppingCarService.addShoppingCarToUser(idUser);

        return "redirect:/shoppingCar";
    }*//*


}
*/
