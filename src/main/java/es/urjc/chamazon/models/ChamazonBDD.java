package es.urjc.chamazon.models;

import es.urjc.chamazon.repositories.CategoryRepository;
import es.urjc.chamazon.services.CategoryService;
import es.urjc.chamazon.services.ProductService;
import es.urjc.chamazon.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
/*
@Component
public class ChamazonBDD implements CommandLineRunner {

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

    @Override
    @Transactional
    public void run(String... args) throws SQLException {
        Category electronics = new Category("Electronics", "Electronic devices");
        Category clothing = new Category("Clothing", "Fashion items");
        Category books = new Category("Books", "A collection of books");
        Category travel = new Category("Travel", "Travel gear");

        /*categoryService.createCategory(electronics);
        categoryService.createCategory(clothing);
        categoryService.createCategory(books);
        categoryService.createCategory(travel);


        categoryRepository.save(electronics);
        categoryRepository.save(clothing);
        categoryRepository.save(books);
        categoryRepository.save(travel);

        Product laptop = new Product("Laptop Premium", 999.99F,
                "High performance laptop with 16GB RAM",
                new SerialBlob(new byte[0]), 4.5F);

        Product smartphone = new Product("Smartphone X", 699.99F,
                "Latest model with AMOLED display",
                new SerialBlob(new byte[0]), 4.7F);

        Product tshirt = new Product("Cotton T-Shirt", 19.99F,
                "Comfortable 100% cotton t-shirt",
                new SerialBlob(new byte[0]), 4.2F);

        Product novel = new Product("Best Seller Novel", 14.99F,
                "The most popular novel this year",
                new SerialBlob(new byte[0]), 4.8F);

        Product backpack = new Product("Travel Backpack", 49.99F,
                "Durable backpack for travelers",
                new SerialBlob(new byte[0]), 4.3F);

        // Guardar productos
        productService.save(laptop);
        productService.save(smartphone);
        productService.save(tshirt);
        productService.save(novel);
        productService.save(backpack);

        categoryService.addProductToCategory(electronics.getId(), laptop.getId());
        categoryService.addProductToCategory(electronics.getId(), smartphone.getId());
        categoryService.addProductToCategory(clothing.getId(), tshirt.getId());
        categoryService.addProductToCategory(books.getId(), novel.getId());
        categoryService.addProductToCategory(travel.getId(), backpack.getId());

        //PÑroducto en muchas categorias:
        // Opcional: Producto en múltiples categorías
        Product smartwatch = new Product("Smart Watch", 199.99F,
                "Fitness tracker with notifications",
                new SerialBlob(new byte[0]), 4.0F);
        productService.save(smartwatch);
        categoryService.addProductToCategory(electronics.getId(), smartwatch.getId());
        categoryService.addProductToCategory(clothing.getId(), smartwatch.getId());



        userService.save(new User("Admin", "PepeAdmin", "Pepe", "Montero", "123", "pepe@mail.com", "098", "q"));
        userService.save(new User("Cliente", "User2", "Maria", "Carrera ", "321", "maria@mail.com", "100", "p"));



        System.out.println("Categorías creadas correctamente.");
    }
}*/