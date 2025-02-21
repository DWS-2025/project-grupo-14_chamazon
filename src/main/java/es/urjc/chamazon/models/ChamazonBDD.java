package es.urjc.chamazon.models;

import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.ProductService;
import es.urjc.chamazon.services.ShopingCarService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ChamazonBDD {

    private final ShopingCarService shopingCarService;
    private final CategoryService categoryService;
    private final ProductService productService;


        public ChamazonBDD(ShopingCarService shopingCarService, ProductService productService, CategoryService categoryService) {
            this.shopingCarService = shopingCarService;
            this.categoryService = categoryService;
            this.productService = productService;

            //listShopingCar.add(shopingCar.getId(),shopingCar);

            //this.ListShopingCar = listShopingCar;
                // 🔹 Inicializar Categorías
            Category electronics = new Category("Electronics");
            Category clothing = new Category("Clothing");

            categoryService.addCategory(electronics.getName());
            categoryService.addCategory(clothing.getName());

            // 🔹 Crear Productos y asignarles Categorías
            Product smartphone = new Product(1, "Smartphone", "High-end smartphone", 499.99, "smartphone.jpg", electronics);
            Product tshirt = new Product(2, "T-shirt", "Comfortable cotton t-shirt", 19.99, "tshirt.jpg", clothing);

            productService.createProduct(smartphone);
            productService.createProduct(tshirt);

            // 🔹 Asignar productos a las categorías
            categoryService.addProductToCategory(smartphone, electronics.getId());
            categoryService.addProductToCategory(tshirt, clothing.getId());

        }

        public void addShopingCar(ShopingCar car) {
            //this.ListShopingCar.add(car);
        }

/*        public List<ShopingCar> getListShopingCar() {
            return ListShopingCar;
        }*/
}
