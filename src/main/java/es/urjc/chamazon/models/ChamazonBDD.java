package es.urjc.chamazon.models;

import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChamazonBDD {



    private List<ShopingCar> ListShopingCar;
    private final CategoryService categoryService;
    private final ProductService productService;


        public ChamazonBDD(ProductService productService, CategoryService categoryService) {
            List<ShopingCar> listShopingCar = new ArrayList<>();
            this.categoryService = categoryService;
            this.productService = productService;

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
            this.ListShopingCar.add(car);
        }

        public List<ShopingCar> getListShopingCar() {
            return ListShopingCar;
        }
}
