package es.urjc.chamazon.models;

import com.mysql.cj.jdbc.Blob;
import es.urjc.chamazon.repositories.CategoryRepository;
import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.ProductService;
import es.urjc.chamazon.services.UserService;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChamazonBDD{

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    public ChamazonBDD(CategoryService categoryService, UserService userService, CategoryRepository categoryRepository, ProductService productService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    @PostConstruct
    public void init() {



        Category electronics = new Category("Electronics", "Electronic devices");
        Category clothing = new Category("Clothing", "Fashion items");
        Category books = new Category("Books", "A collection of books");
        Category travel = new Category("Travel", "Travel gear");


        Product laptop = new Product("Laptop Premium", 999.99F,
                "High performance laptop with 16GB RAM",
                4.5F);

        Product smartphone = new Product("Smartphone X", 699.99F,
                "Latest model with AMOLED display",
                4.7F);

        Product tshirt = new Product("Cotton T-Shirt", 19.99F,
                "Comfortable 100% cotton t-shirt",
                4.2F);

        Product novel = new Product("Best Seller Novel", 14.99F,
                "The most popular novel this year",
                4.8F);

        Product backpack = new Product("Travel Backpack", 49.99F,
                "Durable backpack for travelers",
                4.3F);


        //Safe secondary entity
        categoryService.save(electronics);
        categoryService.save(clothing);
        categoryService.save(books);
        categoryService.save(travel);

        //Assing secondary entity to main entity
        laptop.getCategoryList().add(electronics);
        smartphone.getCategoryList().add(electronics);
        tshirt.getCategoryList().add(clothing);
        novel.getCategoryList().add(books);
        backpack.getCategoryList().add(travel);


        //Producto en muchas categorias:
        // Opcional: Producto en múltiples categorías
        Product smartwatch = new Product("Smart Watch", 199.99F,
                "Fitness tracker with notifications", 4.0F);
        smartwatch.getCategoryList().add(clothing);
        smartwatch.getCategoryList().add(electronics);


        try {

            productService.save(laptop);
            productService.save(smartphone);
            productService.save(tshirt);
            productService.save(novel);
            productService.save(backpack);

            productService.save(smartwatch);

        } catch (Exception e) {
            System.out.println("Error guardando el producto: " + e.getMessage());
        }



        userService.save(new User("Admin", "PepeAdmin", "Pepe", "Montero", "123", "pepe@mail.com", "098", "q"));
        userService.save(new User("Cliente", "User2", "Maria", "Carrera ", "321", "maria@mail.com", "100", "p"));


        System.out.println("Categorías creadas correctamente.");
    }
}