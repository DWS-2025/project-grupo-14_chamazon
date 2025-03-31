
package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.ShoppingCar;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.repositories.ProductRepository;
import es.urjc.chamazon.repositories.ShoppingCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ShoppingCarService {

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

    @Autowired
    private ProductService productService;

    private UserService userService;

    //private ShoppingCar shoppingCar;
    private User user;


    //UTILS//

        protected ShoppingCar firstSC(User user){
            ShoppingCar sc = new ShoppingCar();
            sc.setUser(user);
            shoppingCarRepository.save(sc);
            return sc;
        }

    //ALIAS FOR CRUD REPOSITORY METHODS//

        public void addShoppingCar(ShoppingCar sc) {
            shoppingCarRepository.save(sc);
        }

        public ShoppingCar getShoppingCarById(Long id) {
            Optional<ShoppingCar> scOptional = shoppingCarRepository.findById(id);
            return scOptional.orElse(null);
        }

        public void updateShoppingCar(ShoppingCar sc) {
            shoppingCarRepository.save(sc);
        }

        public void deleteShoppingCar(ShoppingCar sc) {
            shoppingCarRepository.delete(sc);
        }


    //SHOPPING CAR METHODS//

        //Return the shoppingCar with Date = null. If not exist create it and return
        public ShoppingCar getActualShoppingCarByIdUser(Long idUser) {
            if(shoppingCarRepository.existsByUser_IdAndDateSoldNull(idUser)){
                return shoppingCarRepository.findByUser_IdAndDateSoldNull(idUser);
            }else{
                return shoppingCarRepository.save(new ShoppingCar(userService.findById(idUser).get()));
            }

        }

        //Return the ended ShoppingCar Purchase
        //To Check
        public ShoppingCar endPurchaseByIdUser(Long idUser) {
            ShoppingCar sc = getActualShoppingCarByIdUser(idUser);
            return this.endPurchaseBySC(sc);
        }

        public ShoppingCar endPurchaseById(Long idSC) {
            ShoppingCar sc = getShoppingCarById(idSC);
            return this.endPurchaseBySC(sc);
        }

        public ShoppingCar endPurchaseBySC(ShoppingCar sc) {

            if (!sc.getProductList().isEmpty()) {
                sc.setDateSold(LocalDateTime.now());
                shoppingCarRepository.save(sc);
            }
            return sc;
        }


        //Delete Actual ShoppingCar and return it
        public ShoppingCar deleteActualShoppingCarByIdUser(Long idUser) {
            ShoppingCar sc = getActualShoppingCarByIdUser(idUser);
            shoppingCarRepository.deleteById(sc.getId());
            return sc;
        }

        public ShoppingCar addProductToUserShoppingCar(Long idProduct, Long idUser) {
            ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
            Optional <Product> prOpt = productService.findById(idProduct);
            if (prOpt.isPresent()) {
                sc.getProductList().add(prOpt.get());
                shoppingCarRepository.save(sc);
            }
            return sc;
        }

        public ShoppingCar removeProductsFromShoppingCar(Long idProduct, Long idUser, boolean dellAll) {
            ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
            Optional <Product> prOpt = productService.findById(idProduct);
            if (prOpt.isPresent()) {
                sc.getProductList().remove(prOpt.get());
                shoppingCarRepository.save(sc);
            }
            return sc;
        }


    //SHOPPING CAR LIST METHODS//

        public List<ShoppingCar> getShoppingCarListByUser(Long idUser) {
            //ToCheck
            List<ShoppingCar> shoppingCarList = new ArrayList<>();
            Optional<List<ShoppingCar>> scOptional = shoppingCarRepository.findByUser_Id(idUser);
            if (scOptional.isPresent()) {
                shoppingCarList = scOptional.get();
            }
            return shoppingCarList;
        }

        public List<Product> getProductListFromActualShoppingCar(Long idUser) {
            ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
            return getProductListFromSC(sc);
        }

        public List<Product> getProductListByIdShoppingCar(Long idSC) {
            ShoppingCar sc = this.getShoppingCarById(idSC);
            return getProductListFromSC(sc);
        }

        public List<Product> getProductListFromSC(ShoppingCar sc) {
            return sc.getProductList();
        }



        //public Long getShoppingCarSizeFromActualShoppingCar(Long idUser) {}


    //DEPRECATED Methods / Delete on future

    //Create a new List shoppingCar for idUser
/*    private List<ShoppingCar> addListShoppingCarToUser(int idUser) {
        List<ShoppingCar> shoppingCarList = new ArrayList<>();
        shoppingCars.put(idUser, shoppingCarList);
        return shoppingCarList;
    }*/

    //Create a new shoppingCar for idUser
/*    private ShoppingCar addShoppingCarToUser(int idUser) {

        sc.setIdUser(idUser);
        shoppingCars.get(idUser).add(sc);


        List<ShoppingCar> newList = shoppingCars.get(idUser);
        newList.add(sc);
        shoppingCars.put(idUser,newList);

        return sc;
    }*/

/*    public List<Product> getProductListFromidList(List<Integer> idProductList) {
        List<Product> productList = new ArrayList<>();
        if (!idProductList.isEmpty()) {
            for (Integer idP : idProductList) {
                productList.add(productService.findById(idP));
            }
            return productList;
        }
        return productList;
    }*/
}

