package es.urjc.chamazon.restControllers;

import es.urjc.chamazon.dto.*;
import es.urjc.chamazon.services.ShoppingCarService;
import es.urjc.chamazon.services.UserService;
import es.urjc.chamazon.configurations.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/shoppingCar")
public class RestShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private UserService userService;

    private boolean hasAccess(long idUser, Authentication auth) {
        if (SecurityUtils.isAdmin()) {
            return true;
        }
        String username = auth.getName();
        Optional<UserDTOExtended> userOpt = userService.findByUserName(username);
        return userOpt.isPresent() && userOpt.get().id() == idUser;
    }

    // Obtener carrito de compras por ID
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCarExtendedDTO> getShoppingCarById(@PathVariable long id, Authentication auth) {
        try {
            ShoppingCarExtendedDTO sc = shoppingCarService.getShoppingCarDTOById(id);
            if (!SecurityUtils.isAdmin()) {
                String username = auth.getName();
                var userOpt = userService.findByUserName(username);
                if (userOpt.isEmpty() || !Objects.equals(userOpt.get().id(), sc.getUser().id())) {
                    return ResponseEntity.status(403).build();
                }
            }
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener productos en el carrito por ID de carrito
    @GetMapping("/{id}/products")
    public ResponseEntity<List<SimpleProductDTO>> getProductListByShoppingCarId(@PathVariable long id, Authentication auth) {
        try {
            ShoppingCarExtendedDTO sc = shoppingCarService.getShoppingCarDTOById(id);

            if (!SecurityUtils.isAdmin()) {
                String username = auth.getName();
                var userOpt = userService.findByUserName(username);
                if (userOpt.isEmpty() || !Objects.equals(userOpt.get().id(), sc.getUser().id())) {
                    return ResponseEntity.status(403).build();
                }
            }

            List<SimpleProductDTO> products = shoppingCarService.getProductListDTOByShoppingCarId(id);
            return ResponseEntity.ok(products);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }


    // Obtener carrito actual de un usuario
    @GetMapping("/user/{idUser}")
    public ResponseEntity<ShoppingCarDTO> getActualShoppingByIdUser(@PathVariable long idUser, Authentication auth) {
        if (!hasAccess(idUser, auth)) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        try {
            ShoppingCarDTO sc = shoppingCarService.getActualShoppingCarDTOByIdUser(idUser);
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener historial de compras del usuario
    @GetMapping("/user/{idUser}/history")
    public ResponseEntity<List<ShoppingCarExtendedDTO>> getAllShoppingCarsByUser(@PathVariable long idUser, Authentication auth) {
        if (!hasAccess(idUser, auth)) {
            return ResponseEntity.status(403).build();
        }

        try {
            List<ShoppingCarExtendedDTO> shoppingCars = shoppingCarService.getShoppingCarDTOListByUserId(idUser);
            return ResponseEntity.ok(shoppingCars);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Agregar producto al carrito de usuario
    @PutMapping("/user/{idUser}/product/{idProduct}")
    public ResponseEntity<ShoppingCarDTO> addProductToShoppingCar(@PathVariable long idUser, @PathVariable long idProduct, Authentication auth) {
        if (!hasAccess(idUser, auth)) {
            return ResponseEntity.status(403).build();
        }

        try {
            ShoppingCarDTO sc = shoppingCarService.addProductToUserShoppingCar(idProduct, idUser);
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar producto del carrito del usuario
    @DeleteMapping("/user/{idUser}/product/{idProduct}")
    public ResponseEntity<ShoppingCarDTO> deleteProductFromShoppingCar(@PathVariable long idUser, @PathVariable long idProduct, Authentication auth) {
        if (!hasAccess(idUser, auth)) {
            return ResponseEntity.status(403).build();
        }

        try {
            ShoppingCarDTO sc = shoppingCarService.removeProductsFromShoppingCar(idProduct, idUser, false);
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Finalizar compra del carrito actual del usuario
    @PutMapping("/user/{idUser}/endPurchase")
    public ResponseEntity<ShoppingCarDTO> endPurchaseByIdUser(@PathVariable long idUser, Authentication auth) {
        if (!hasAccess(idUser, auth)) {
            return ResponseEntity.status(403).build();
        }

        try {
            ShoppingCarDTO sc = shoppingCarService.endPurchaseByIdUser(idUser);
            return ResponseEntity.ok(sc);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar producto y devolver ID del nuevo carrito
    @DeleteMapping("/user/{idUser}/product/{idProduct}/redirect")
    public ResponseEntity<Long> deleteProductAndGetShoppingCarId(@PathVariable long idUser, @PathVariable long idProduct, Authentication auth) {
        if (!hasAccess(idUser, auth)) {
            return ResponseEntity.status(403).build();
        }

        try {
            shoppingCarService.removeProductsFromShoppingCar(idProduct, idUser, false);
            Long newCartId = shoppingCarService.getActualShoppingCarDTOByIdUser(idUser).getId();
            return ResponseEntity.ok(newCartId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
