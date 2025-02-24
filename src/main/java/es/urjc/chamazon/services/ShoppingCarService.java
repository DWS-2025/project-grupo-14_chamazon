package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.ShoppingCar;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class ShoppingCarService {

    private final ProductService productService;
    private ConcurrentHashMap<Integer, List<ShoppingCar>> shoppingCars = new ConcurrentHashMap<>();

    public ShoppingCarService(ProductService productService) {
        this.productService = productService;
    }

    //TODO
    public ShoppingCar getDatedShoppingCarByIdUser(int idUser, DateTimeFormatter dateSold) {
        return null;
    }

    //Create a new ShoppingCar for the user. If a list does not exist for that user, it creates one
    public ShoppingCar addNewSoppingCarToUser(int idUser){
        if (!shoppingCars.containsKey(idUser)) {
            this.addListShoppingCarToUser(idUser);
            return this.addShoppingCarToUserList(idUser);
        }else{
            //If not exist ShoppingCar it creates one
            if (getActualShoppingCarByIdUser(idUser) == null) {
                return this.addShoppingCarToUserList(idUser);

            //Else returns null for know that already one created
            // Devuelve nulo para saber que ya hay uno creado
            }else{
                return null;
            }
        }
    }

    //Return the shoppingCar with Date = null. If not exist create it and return
    public ShoppingCar getActualShoppingCarByIdUser(int idUser) {
        List<ShoppingCar> listSC = shoppingCars.get(idUser);
        if (!listSC.isEmpty()) {
            for (ShoppingCar spc : listSC) {
                if (spc.getDateSold() == null){
                    return spc;
                }
            }
            return this.addShoppingCarToUserList(idUser);
        }else{
            return this.addShoppingCarToUserList(idUser);
        }
    }

    //Return the ended ShoppingCar Purchase
    //To Check
    public ShoppingCar endPurchaseByIdUser(int idUser) {
        ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
        sc.setDateSold(DateTimeFormatter.ofPattern(DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())));
        this.addNewSoppingCarToUser(idUser);
        return sc;
    }


    //Delete Actual ShoppingCar and return it
    public ShoppingCar deleteActualShoppingCarByIdUser(int idUser) {
        ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
        this.shoppingCars.get(idUser).remove(sc);
        return sc;

    }

    public ShoppingCar addProductFromShoppingCarByIdUser(int idUser, Product product) {
        ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
        sc.getProducts().add(product.getId());

        this.deleteActualShoppingCarByIdUser(idUser);
        this.shoppingCars.get(idUser).add(sc);

        return sc;
    }


    public ShoppingCar removeProductsFromShoppingCar(int idUser, int idProduct, boolean dellAll) {
        ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
        if (sc.getProducts().contains(idProduct)) {

            if (dellAll) {
                sc.getProducts().removeIf(p -> p == idProduct);
            }else{
                sc.getProducts().remove(idProduct);
            }
            this.deleteActualShoppingCarByIdUser(idUser);
            this.shoppingCars.get(idUser).add(sc);
            return sc;

        }
        return sc;
    }

    public List<Product> getProductListFromActualShoppingCar(int idUser) {
        List<Product> productList = new ArrayList<>();
        ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
        for (Integer idP : sc.getProducts()) {
            productList.add(productService.getProduct(idP));
        }
        return productList;
    }

    //Create a new List shoppingCar for idUser
    private List<ShoppingCar> addListShoppingCarToUser(int idUser) {
        List<ShoppingCar> shoppingCarList = new ArrayList<>();
        shoppingCars.put(idUser, shoppingCarList);
        return shoppingCarList;
    }

    //Create a new shoppingCar for idUser
    private ShoppingCar addShoppingCarToUserList(int idUser) {
        ShoppingCar sc = new ShoppingCar();
        shoppingCars.get(idUser).add(sc);

/*        List<ShoppingCar> newList = shoppingCars.get(idUser);
        newList.add(sc);
        shoppingCars.put(idUser,newList);*/
        return sc;
    }

}
