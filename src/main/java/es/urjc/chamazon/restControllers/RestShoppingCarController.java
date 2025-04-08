package es.urjc.chamazon.restControllers;

import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.ProductDTOExtended;
import es.urjc.chamazon.dto.ShoppingCarDTO;
import es.urjc.chamazon.dto.ShoppingCarExtendedDTO;
import es.urjc.chamazon.models.ShoppingCar;
import es.urjc.chamazon.services.ShoppingCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Boolean.FALSE;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/shoppingCar")
public class RestShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;

    @GetMapping("/actual/{idUser}")
    public ResponseEntity<ShoppingCarDTO> getActualShoppingByIdUser(@PathVariable long idUser){
        ShoppingCarDTO sc = shoppingCarService.getActualShoppingCarDTOByIdUser(idUser);
        try{
            return ResponseEntity.ok(sc);
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<List<ProductDTO>> getProductListByShoppingCarId(@PathVariable long id){
        List<ProductDTO> sc = shoppingCarService.getProductListDTOByShoppingCarId(id);
        try {
            return ResponseEntity.ok(sc);
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/addProduct/{idUser}/{idProduct}")
    public ResponseEntity<ShoppingCarDTO> addProductToShoppingCar(@PathVariable long idUser, @PathVariable long idProduct){
        try {
            ShoppingCarDTO sc = shoppingCarService.addProductToUserShoppingCar(idProduct, idUser);
            return ResponseEntity.ok(sc);
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/endPurchase/{idUser}")
    public ResponseEntity<ShoppingCarDTO> endPurchaseByIdUser(@PathVariable long idUser){
        try {
            return ResponseEntity.ok(shoppingCarService.endPurchaseByIdUser(idUser));
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/remove/{idProduct}/{idUser}")
    public ResponseEntity<ShoppingCarDTO> deleteProductFromShoppingCar(@PathVariable long idProduct, @PathVariable long idUser){
        ShoppingCarDTO sc = shoppingCarService.removeProductsFromShoppingCar(idProduct, idUser, false);
        return ResponseEntity.ok(sc);
    }











    @GetMapping("/history/{idUser}")
    public ResponseEntity<List<ShoppingCarExtendedDTO>> getAllShoppinngCarsByUser(@PathVariable long idUser) {
        List<ShoppingCarExtendedDTO> shoppingCars = shoppingCarService.getShoppingCarDTOListByUserId(idUser);
        try {
            return ResponseEntity.ok(shoppingCars);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCarExtendedDTO> getShoppingCarById(@PathVariable long id){
        ShoppingCarExtendedDTO sc = shoppingCarService.getShoppingCarDTOById(id);
        try{
            return ResponseEntity.ok(sc);
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }
}
