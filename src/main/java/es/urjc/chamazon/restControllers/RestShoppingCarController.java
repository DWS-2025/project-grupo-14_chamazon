package es.urjc.chamazon.restControllers;

import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.ShoppingCarDTO;
import es.urjc.chamazon.dto.ShoppingCarExtendedDTO;
import es.urjc.chamazon.dto.SimpleProductDTO;
import es.urjc.chamazon.services.ShoppingCarService;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/shoppingCar")
public class RestShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private UserService userService;

    // Obtener carrito de compras por ID
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCarExtendedDTO> getShoppingCarById(@PathVariable long id){
        try {
            ShoppingCarExtendedDTO sc = shoppingCarService.getShoppingCarDTOById(id);
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener productos en el carrito por ID de carrito
    @GetMapping("/{id}/products")
    public ResponseEntity<List<SimpleProductDTO>> getProductListByShoppingCarId(@PathVariable long id){
        try {
            List<SimpleProductDTO> sc = shoppingCarService.getProductListDTOByShoppingCarId(id);
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener carrito actual de un usuario
    @GetMapping("/user/{idUser}")
    public ResponseEntity<ShoppingCarDTO> getActualShoppingByIdUser(@PathVariable long idUser){
        try {
            ShoppingCarDTO sc = shoppingCarService.getActualShoppingCarDTOByIdUser(idUser);
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener historial de compras del usuario
    @GetMapping("/user/{idUser}/history")
    public ResponseEntity<List<ShoppingCarExtendedDTO>> getAllShoppingCarsByUser(@PathVariable long idUser) {
        try {
            List<ShoppingCarExtendedDTO> shoppingCars = shoppingCarService.getShoppingCarDTOListByUserId(idUser);
            return ResponseEntity.ok(shoppingCars);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Agregar producto al carrito de usuario
    @PutMapping("/user/{idUser}/product/{idProduct}")
    public ResponseEntity<ShoppingCarDTO> addProductToShoppingCar(@PathVariable long idUser, @PathVariable long idProduct){
        try {
            ShoppingCarDTO sc = shoppingCarService.addProductToUserShoppingCar(idProduct, idUser);
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar producto del carrito del usuario
    @DeleteMapping("/user/{idUser}/product/{idProduct}")
    public ResponseEntity<ShoppingCarDTO> deleteProductFromShoppingCar(@PathVariable long idUser, @PathVariable long idProduct){
        try {
            ShoppingCarDTO sc = shoppingCarService.removeProductsFromShoppingCar(idProduct, idUser, false);
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    // Finalizar compra del carrito actual del usuario
    @PutMapping("/user/{idUser}/endPurchase")
    public ResponseEntity<ShoppingCarDTO> endPurchaseByIdUser(@PathVariable long idUser){
        try {
            ShoppingCarDTO sc = shoppingCarService.endPurchaseByIdUser(idUser);
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    // NUEVO: Eliminar un producto del carrito y devolver ID del nuevo carrito
    @DeleteMapping("/user/{idUser}/product/{idProduct}/redirect")
    public ResponseEntity<Long> deleteProductAndGetShoppingCarId(@PathVariable long idUser, @PathVariable long idProduct){
        try {
            shoppingCarService.removeProductsFromShoppingCar(idProduct, idUser, false);
            Long newCartId = shoppingCarService.getActualShoppingCarDTOByIdUser(idUser).getId();
            return ResponseEntity.ok(newCartId);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

}
