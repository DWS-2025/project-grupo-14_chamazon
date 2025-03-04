package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.ShoppingCar;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<ShoppingCar> getShoppingCarListByUser(int idUser) {
        List<ShoppingCar> sc = shoppingCars.get(idUser);
        if (sc == null) {
            this.addNewSoppingCarToUser(idUser);
            return shoppingCars.get(idUser);
        }else {
            return sc;
        }
    }

    public ShoppingCar getShoppingCarById(int id) {
        for (List<ShoppingCar> list : shoppingCars.values()) {
            for (ShoppingCar sc : list) {
                if (sc.getId() == id) {
                    return sc;
                }
            }
        }
        return null;
    }

    public Map<Integer, Integer> getProductsLengthMap(List<ShoppingCar> listShoppingCar) {
        Map<Integer, Integer> lengthMap = new HashMap<>();
        for (ShoppingCar shoppingCar : listShoppingCar) {
            if (shoppingCar.getProducts() != null) {
                lengthMap.put(shoppingCar.getId(), shoppingCar.getProducts().size());
            }
        }
        return lengthMap;
    }

    //Create a new ShoppingCar for the user. If a list does not exist for that user, it creates one
    public ShoppingCar addNewSoppingCarToUser(int idUser){
        if (!shoppingCars.containsKey(idUser)) {
            this.addListShoppingCarToUser(idUser);
            return this.addShoppingCarToUserList(idUser);
        }else{
            //If not exist ShoppingCar it creates one
            if (shoppingCars.get(idUser).isEmpty()) {
                return this.addShoppingCarToUserList(idUser);

            }else{
                return getActualShoppingCarByIdUser(idUser);
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
        return this.endPurchaseBySC(sc);
    }
    public ShoppingCar endPurchaseById(int idSC) {
        ShoppingCar sc = this.getShoppingCarById(idSC);
        return this.endPurchaseBySC(sc);
    }
    public ShoppingCar endPurchaseBySC(ShoppingCar sc) {
        if (sc.getDateSold() == null) {
            sc.setDateSold(LocalDateTime.now());
            this.addNewSoppingCarToUser(sc.getIdUser());
            return sc;
        }
        return sc;
    }


    //Delete Actual ShoppingCar and return it
    public ShoppingCar deleteActualShoppingCarByIdUser(int idUser) {
        ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
        this.shoppingCars.get(idUser).remove(sc);
        return sc;
    }

    public ShoppingCar addProductToUserShoppingCar(int idProduct, int idUser) {
        ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
        sc.getProducts().add(idProduct);
        return sc;
    }


    public ShoppingCar removeProductsFromShoppingCar(int idProduct, int idUser, boolean dellAll) {
        ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
        if (sc.getProducts().contains(idProduct)) {

            if (dellAll) {
                sc.getProducts().removeIf(p -> p == idProduct);
            }else{
                if (sc.getProducts().size() < 2) {
                    sc.getProducts().clear();
                }else{
                    sc.getProducts().remove(idProduct);
                }
            }
/*            this.deleteActualShoppingCarByIdUser(idUser);
            this.shoppingCars.get(idUser).add(sc);*/
            return sc;

        }
        return sc;
    }

    public List<Product> getProductListFromActualShoppingCar(int idUser) {
        ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
        return getProductListFromidList(sc.getProducts());
    }

    public List<Product> getProductListByIdShoppingCar(int idSC) {
        ShoppingCar sc = this.getShoppingCarById(idSC);
        return getProductListFromidList(sc.getProducts());
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
        sc.setIdUser(idUser);
        shoppingCars.get(idUser).add(sc);

/*        List<ShoppingCar> newList = shoppingCars.get(idUser);
        newList.add(sc);
        shoppingCars.put(idUser,newList);*/
        return sc;
    }

    public List<Product> getProductListFromidList(List<Integer> idProductList) {
        List<Product> productList = new ArrayList<>();
        if (!idProductList.isEmpty()) {
            for (Integer idP : idProductList) {
                productList.add(productService.findById(idP));
            }
            return productList;
        }
        return productList;
    }
}
