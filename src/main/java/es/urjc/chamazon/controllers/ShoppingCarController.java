package es.urjc.chamazon.controllers;

import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.ShoppingCarDTO;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.services.ShoppingCarService;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Boolean.FALSE;

@Controller
@RequestMapping("/shoppingCar")
public class ShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private UserService userService;


    @GetMapping("/history/{idUser}")
    public String shoppingCarHistory (@PathVariable Long idUser, Model model) {
        model.addAttribute("userName", userService.getUser(idUser).userName());
        List<ShoppingCarDTO> listShoppingCarDTO = shoppingCarService.getShoppingCarDTOListByUserId(idUser);

        model.addAttribute("listShoppingCarDTO", listShoppingCarDTO);
        //model.addAttribute("productLengthMap", shoppingCarService.getProductsLengthMap(listShoppingCarDTO));

        return "shoppingCar/shoppingCarHistory";

    }

    @GetMapping("/{id}")
    public String shoppingCar (@PathVariable Long id, Model model) {
        model.addAttribute("title", "Shopping Car");
        ShoppingCarDTO sc = shoppingCarService.getShoppingCarDTOById(id);
        if (sc != null) {
            model.addAttribute("ShoppingCarDTO", sc);
        }
        List<ProductDTO> productList = shoppingCarService.getProductListDTOByShoppingCarId(id);

        model.addAttribute("idSC", id);
        model.addAttribute("ifNotEnd", sc.getDateSold());
        // model.addAttribute("ifNotProducts", sc.getProducts().isEmpty());
        model.addAttribute("idUser", sc.getUser().id());
        model.addAttribute("productList", productList);

        return "shoppingCar/shoppingCar";
    }

/*    @PutMapping("/endPurchase/{idUser}")
    public String endPurchase (@PathVariable Long idUser,Model model) {
        shoppingCarService.endPurchaseByIdUser(idUser);
        return "redirect:/shoppingCar/History/" + idUser;
    }

    @PutMapping("/removeProduct/{idProduct}")
    public String removeProduct (@PathVariable Long idProduct, @PathVariable Long idUser, Model model) {

        shoppingCarService.removeProductsFromShoppingCar(idProduct, idUser, FALSE);
        Long idSC = shoppingCarService.getActualShoppingCarDTOByIdUser(idUser).getId();

        return "/shoppingCar/" + idSC;
    }*/


/*    @PutMapping("/shoppingCar")
    public String updateShoppingCar (@RequestBody ShoppingCarDTO shoppingCarDTO, @RequestParam int idUser, Model model) {

        return "redirect:/shoppingCar";
    }*/



    /*    @PostMapping("/shoppingCar/")
    public String createShoppingCar (@RequestParam int idUser, @RequestParam ConcurrentMap<Integer, Product> products, Model model) {

        shoppingCarService.addShoppingCarToUser(idUser);

        return "redirect:/shoppingCar";
    }*/


}
