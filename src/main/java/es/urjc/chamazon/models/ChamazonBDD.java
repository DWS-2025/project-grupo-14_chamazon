package es.urjc.chamazon.models;

import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.ProductService;
import es.urjc.chamazon.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChamazonBDD {

    private List<ShopingCar> ListShopingCar;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;


        public ChamazonBDD(ProductService productService, CategoryService categoryService, UserService userService) {
            List<ShopingCar> listShopingCar = new ArrayList<>();
            this.categoryService = categoryService;
            this.productService = productService;
            this.userService = userService ;

                Object product = new Object();
                List<Object> products = new ArrayList<>();
                ShopingCar shopingCar = new ShopingCar(1,products);
                listShopingCar.add(shopingCar);

                this.ListShopingCar = listShopingCar;

                // 🔹 Inicializar Categorías
            Category electronics = new Category("Electronics");
            Category clothing = new Category("Clothing");

            categoryService.addCategory(electronics.getName());
            categoryService.addCategory(clothing.getName());

            // 🔹 Inicializar Productos
            Product smartphone = new Product(2, "Smartphone", "High-end smartphone", 499.99, "smartphone.jpg", electronics);
            Product tshirt = new Product(3, "T-shirt", "Comfortable cotton t-shirt", 19.99, "tshirt.jpg", clothing);

            productService.addProduct(smartphone);
            productService.addProduct(tshirt);

            // 🔹 Asignar productos a las categorías
            categoryService.addProductToCategory(smartphone, electronics.getId());
            categoryService.addProductToCategory(tshirt, clothing.getId());

            User user1 = new User("user1", "user1@gmail.com");
            User user2 = new User("user2", "user2@gmail.com");

            userService.addUser(user1);
            userService.addUser(user2);

        }

        public void addShopingCar(ShopingCar car) {
            this.ListShopingCar.add(car);
        }

        public List<ShopingCar> getListShopingCar() {
            return ListShopingCar;
        }
}
