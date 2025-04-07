
package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.ProductMapper;
import es.urjc.chamazon.dto.ShoppingCarDTO;
import es.urjc.chamazon.dto.ShoppingCarMapper;
import es.urjc.chamazon.dto.*;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.ShoppingCar;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.repositories.ShoppingCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class ShoppingCarService {

    private final ShoppingCarMapper shoppingCarMapper;
    private final ProductMapper productMapper;
    private final ShoppingCarRepository shoppingCarRepository;
    private final ProductService productService;
    private final UserService userService;// <- FINAL private final
    UserMapper userMapper;

    @Autowired public ShoppingCarService( ShoppingCarMapper shoppingCarMapper, ProductMapper productMapper, ShoppingCarRepository shoppingCarRepository, ProductService productService, UserMapper userMapper, UserService userService ) {
        this.shoppingCarMapper = shoppingCarMapper;
        this.productMapper = productMapper;
        this.shoppingCarRepository = shoppingCarRepository;
        this.productService = productService;
        this.userService = userService;
        this.userMapper = userMapper;
    }



    //private ShoppingCar shoppingCar;
    private User user;


    //ALIAS FOR CRUD REPOSITORY METHODS//

        public ShoppingCar firstSC(UserDTO user){
            ShoppingCar sc = new ShoppingCar();
            sc.setUser(userMapper.toUser(user));
            sc.setDateSold(null);
            addShoppingCar(sc);
            return shoppingCarRepository.save(sc);
        }
        void addShoppingCar(ShoppingCar sc) {
            shoppingCarRepository.save(sc);
        }

        ShoppingCar getShoppingCarById(Long id) {
            Optional<ShoppingCar> scOptional = shoppingCarRepository.findById(id);
            return scOptional.orElseThrow();
        }

        void updateShoppingCar(ShoppingCar sc) {
            shoppingCarRepository.save(sc);
        }

        void deleteShoppingCar(ShoppingCar sc) {
            shoppingCarRepository.delete(sc);
        }


    //ALIAS FOR DTOs METHODS//
        ShoppingCarDTO toDTOById(Long id){
            ShoppingCar sc = getShoppingCarById(id);
            return toDTO(sc);
        }
        ShoppingCarDTO toDTO(ShoppingCar sc){
            return shoppingCarMapper.toDTO(sc);
        }

        ShoppingCar toDomain(ShoppingCarDTO shoppingCarDTO){
            return shoppingCarMapper.toDomain(shoppingCarDTO);
        }

        List<ShoppingCarDTO> toDTOList(List<ShoppingCar> scList){
            return shoppingCarMapper.toDTOs(scList);
        }

    //SHOPPING CAR METHODS//

        public ShoppingCarDTO getShoppingCarDTOById(Long id) {
            return toDTO(getShoppingCarById(id));
        }

        public ShoppingCarDTO getActualShoppingCarDTOByIdUser(Long id) {
            return toDTO(getActualShoppingCarByIdUser(id));
        }
        ShoppingCar getActualShoppingCarByIdUser(long idUser) {
            if (shoppingCarRepository.existsByUser_IdAndDateSoldNull(idUser)) {
                return shoppingCarRepository.findByUser_IdAndDateSoldNull(idUser);
            } else {
                User user = userService.getUserById(idUser);  // asegúrate de que devuelve algo válido
                ShoppingCar sc = new ShoppingCar();
                sc.setUser(user);
                return shoppingCarRepository.save(sc);
            }
        }
        //Return the shoppingCar with Date = null. If not exist create it and return
        ShoppingCar ygetActualShoppingCarByIdUser(long idUser) {
            if(shoppingCarRepository.existsByUser_IdAndDateSoldNull(idUser)){
                return shoppingCarRepository.findByUser_IdAndDateSoldNull(idUser);
            } else if (shoppingCarRepository.existsByUser_Id(idUser)){
                return shoppingCarRepository.save(new ShoppingCar(userService.getUserById(idUser)));
            }
            return null;
        }

        //Return the ended ShoppingCar Purchase
        //To Check
        public ShoppingCarDTO endPurchaseByIdUser(Long idUser) {
            ShoppingCar sc = getActualShoppingCarByIdUser(idUser);
            return this.endPurchaseBySC(sc);
        }

        public ShoppingCarDTO endPurchaseById(Long idSC) {
            ShoppingCar sc = getShoppingCarById(idSC);
            return this.endPurchaseBySC(sc);
        }

        public ShoppingCarDTO endPurchaseBySC(ShoppingCar sc) {

            if (!sc.getProductList().isEmpty()) {
                sc.setDateSold(LocalDateTime.now());
                shoppingCarRepository.save(sc);
            }
            return toDTO(sc);
        }


        //Delete Actual ShoppingCar and return it
        public ShoppingCarDTO deleteActualShoppingCarByIdUser(Long idUser) {
            ShoppingCar sc = getActualShoppingCarByIdUser(idUser);
            shoppingCarRepository.deleteById(sc.getId());
            return toDTO(sc);
        }

        public ShoppingCarDTO addProductToUserShoppingCar(Long idProduct, Long idUser) {
            ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
            Product product = productService.findById(idProduct)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Asegúrate de que la lista esté inicializada
            if(sc.getProductList() == null) {
                sc.setProductList(new ArrayList<>());
            }

            // Añade el producto si no está ya en la lista
            if(!sc.getProductList().contains(product)) {
                sc.getProductList().add(product);
                shoppingCarRepository.save(sc);
            }
            return toDTO(sc);
        }

        public ShoppingCarDTO removeProductsFromShoppingCar(Long idProduct, Long idUser, boolean dellAll) {
            ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
            Optional <Product> prOpt = productService.findById(idProduct);
            if (prOpt.isPresent()) {
                sc.getProductList().remove(prOpt.get());
                shoppingCarRepository.save(sc);
            }
            return toDTO(sc);
        }


    //SHOPPING CAR LIST METHODS//

        public List<ShoppingCarDTO> getShoppingCarDTOListByUserId(Long idUser){
            return toDTOList(getShoppingCarListByUserId(idUser));
        }

        List<ShoppingCar> getShoppingCarListByUserId(Long idUser) {
            //ToCheck
            System.out.println(idUser);
            List<ShoppingCar> shoppingCarList = new ArrayList<>();
            Optional<List<ShoppingCar>> scOptional = shoppingCarRepository.findByUser_Id(idUser);
            if (scOptional.isPresent()) {
                shoppingCarList = scOptional.get();
                System.out.println(shoppingCarList);
            }
            return shoppingCarList;
        }


        List<ProductDTO> getProductListFromActualShoppingCar(Long idUser) {
            ShoppingCar sc = this.getActualShoppingCarByIdUser(idUser);
            return getProductListFromSC(sc);
        }

        public List<ProductDTO> getProductListByShoppingCarId(Long idSC) {
            ShoppingCar sc = this.getShoppingCarById(idSC);
            return getProductListFromSC(sc);
        }

        private List<ProductDTO> getProductListFromSC(ShoppingCar sc) {
            return (List<ProductDTO>) productMapper.toDTOs(sc.getProductList());
        }

    public Map<Long, Integer> getProductsLengthMap(List<ShoppingCarDTO> listShoppingCarDTO) {
        Map<Long, Integer> productLengthMap = new HashMap<>();
        for (ShoppingCarDTO shoppingCarDTO : listShoppingCarDTO) {
            productLengthMap.put(shoppingCarDTO.getId(), shoppingCarDTO.getProductList().size());
        }
        return productLengthMap;
    }



    /*public List<ProductDTO> getProductDTOListFromSC(ShoppingCar sc) {
            return productMapping.toDYO(sc.getProductList());
        }


    //UTILS//

    ShoppingCar firstSC(User user){
        ShoppingCar sc = new ShoppingCar();
        sc.setUser(user);
        this.addShoppingCar(sc);
        return sc;
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

