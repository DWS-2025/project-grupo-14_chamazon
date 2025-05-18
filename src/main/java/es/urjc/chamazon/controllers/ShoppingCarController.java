package es.urjc.chamazon.controllers;

import es.urjc.chamazon.dto.*;
import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.services.ShoppingCarService;
import es.urjc.chamazon.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.server.csrf.CsrfToken;
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
    public String shoppingCarHistory (@PathVariable Long idUser, Model model, HttpServletRequest request) {

        //TODO
/*        if (request.isUserInRole("ADMIN")) {

        }*/

/*        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());*/

        model.addAttribute("userName", userService.getUser(idUser).userName());
        List<ShoppingCarExtendedDTO> listShoppingCarDTO = shoppingCarService.getShoppingCarDTOListByUserId(idUser);

        model.addAttribute("listShoppingCarDTO", listShoppingCarDTO);
        //model.addAttribute("productLengthMap", shoppingCarService.getProductsLengthMap(listShoppingCarDTO));

        return "shoppingCar/shoppingCarHistory";

    }

    @GetMapping("/{id}")
    public String shoppingCar (@PathVariable Long id, Model model) {
        model.addAttribute("title", "Shopping Car");
        ShoppingCarExtendedDTO sc = shoppingCarService.getShoppingCarDTOById(id);
        if (sc != null) {
            model.addAttribute("ShoppingCarDTO", sc);
        }
        List<SimpleProductDTO> productList = shoppingCarService.getProductListDTOByShoppingCarId(id);

        model.addAttribute("idSC", id);
        model.addAttribute("ifNotEnd", sc.getDateSold());
        model.addAttribute("ifNotProducts", sc.getProductList().isEmpty());
        model.addAttribute("idUser", sc.getUser().id());
        model.addAttribute("productList", productList);

        return "shoppingCar/shoppingCar";
    }

    @GetMapping("/endPurchase/{idUser}")
    public String endPurchase (@PathVariable Long idUser,Model model) {
        shoppingCarService.endPurchaseByIdUser(idUser);
        return "redirect:/shoppingCar/history/" + idUser;
    }

    @GetMapping("/removeProduct/{idProduct}/{idUser}")
    public String removeProduct (@PathVariable Long idProduct, @PathVariable Long idUser, Model model) {

        shoppingCarService.removeProductsFromShoppingCar(idProduct, idUser, FALSE);
        Long idSC = shoppingCarService.getActualShoppingCarDTOByIdUser(idUser).getId();

        return "/shoppingCar/" + idSC;
    }


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
