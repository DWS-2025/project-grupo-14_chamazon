/*
package es.urjc.chamazon.models;

import es.urjc.chamazon.repositories.ShoppingCarRepository;
import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.ProductService;
import es.urjc.chamazon.services.ShoppingCarService;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;

@Controller
public class ChamazonBDD implements CommandLineRunner {

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

    //private final ShoppingCarService shoppingCarService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;


    @Override
    public void run(String... args) throws Exception {

        shoppingCarRepository.save(new ShoppingCar(1L));
        shoppingCarRepository.save(new ShoppingCar(2L));

        List<ShoppingCar> shoppingCars = shoppingCarRepository.findAll();
        for (ShoppingCar sc : shoppingCars) {
            System.out.println(sc.toString());
        }

        shoppingCarRepository.deleteAll();
    }

        public ChamazonBDD(ProductService productService, CategoryService categoryService, UserService userService,
                            ShoppingCarService shoppingCarService) {
            this.shoppingCarService = shoppingCarService;
            this.categoryService = categoryService;
            this.productService = productService;
            this.userService = userService ;


            Category electronics = new Category("Electronics");
            Category clothing = new Category("Clothing");

            categoryService.addCategory(electronics.getName());
            categoryService.addCategory(clothing.getName());

            Product smartphone = productService.addProduct("Smartphone", "High-end smartphone", 499.99, categoryService.getCategoryByName("Electronics"), "smartphone.jpg");
            Product tshirt = productService.addProduct("T-shirt", "Comfortable cotton t-shirt", 19.99, categoryService.getCategoryByName("Clothing"), "tshirt.jpg");

            categoryService.addProductToCategory(smartphone, electronics.getId());
            categoryService.addProductToCategory(tshirt, clothing.getId());

            User user1 = new User("user1", "user1@gmail.com", "123", null, null);
            User user2 = new User("user2", "user2@gmail.com", "123", null, null);

            userService.addUser(user1);
            userService.addUser(user2);

            shoppingCarService.addProductToUserShoppingCar(1,1);
            shoppingCarService.addProductToUserShoppingCar(1,2);
            shoppingCarService.addProductToUserShoppingCar(1,2);
            shoppingCarService.addProductToUserShoppingCar(1,1);
            shoppingCarService.endPurchaseByIdUser(1);
            shoppingCarService.addProductToUserShoppingCar(1,1);
            shoppingCarService.addProductToUserShoppingCar(1,1);
            shoppingCarService.addProductToUserShoppingCar(1,1);

        }


}
*/
