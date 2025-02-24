package es.urjc.chamazon.models;

import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.ProductService;
import es.urjc.chamazon.services.ShoppingCarService;
import es.urjc.chamazon.services.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChamazonBDD {

    private final ShoppingCarService shoppingCarService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;


        public ChamazonBDD(ProductService productService, CategoryService categoryService, UserService userService,
                            ShoppingCarService shoppingCarService) {
            this.shoppingCarService = shoppingCarService;
            this.categoryService = categoryService;
            this.productService = productService;
            this.userService = userService ;


                // 🔹 Inicializar Categorías
            Category electronics = new Category("Electronics");
            Category clothing = new Category("Clothing");

            categoryService.addCategory(electronics.getName());
            categoryService.addCategory(clothing.getName());

            // 🔹 Inicializar Productos
            Product smartphone = productService.addProduct("Smartphone", "High-end smartphone", 499.99, categoryService.getCategoryByName("Electronics"), "smartphone.jpg");
            Product tshirt = productService.addProduct("T-shirt", "Comfortable cotton t-shirt", 19.99, categoryService.getCategoryByName("Clothing"), "tshirt.jpg");

            // 🔹 Asignar productos a las categorías
            categoryService.addProductToCategory(smartphone, electronics.getId());
            categoryService.addProductToCategory(tshirt, clothing.getId());

            User user1 = new User("user1", "user1@gmail.com", "123");
            User user2 = new User("user2", "user2@gmail.com", "123");

            userService.addUser(user1);
            userService.addUser(user2);

        }

}
