package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.dto.CommentMapper;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.repositories.CategoryRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChamazonBDD{

    @Autowired
    private CommentMapper commentMapper;


    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ShoppingCarService shoppingCarService;
    private final CommentService commentService;

    public ChamazonBDD(CategoryService categoryService, UserService userService, ShoppingCarService shoppingCarService, ProductService productService, CommentService commentService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.shoppingCarService = shoppingCarService;
        this.productService = productService;
        this.commentService = commentService;
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


        Product raton = new Product("Raton", 9.99F,
                "Raton para el ordenador",
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



        userService.saveUser(new User("Admin", "PepeAdmin", "Pepe", "Montero", "123", "pepe@mail.com", "098", "q"));
        userService.saveUser(new User("Cliente", "User2", "Maria", "Carrera ", "321", "maria@mail.com", "100", "p"));

        //shoppingCarService.addProductToUserShoppingCar(1L,1L);


        System.out.println("Categorías creadas correctamente.");

        User user1 = userService.getUserById(1L);
        User user2 = userService.getUserById(2L);

        Product product1 = productService.findById(1L).orElse(null);
        Product product2 = productService.findById(2L).orElse(null);
        Product product3 = productService.findById(3L).orElse(null);

        if (user1 != null && user2 != null && product1 != null && product2 != null && product3 != null) {

            CommentDTO c1 = commentMapper.toDTO(new Comment("Me encantó este producto, ¡lo uso cada día!", 5, user1, product1)); //Es PepeAdmin
            CommentDTO c2 = commentMapper.toDTO(new Comment("Podría ser mejor, pero está bien", 3, user2, product2)); //Es User2
            CommentDTO c3 = commentMapper.toDTO(new Comment("Excelente relación calidad-precio", 4, user1, product3)); //Es PepeAdmin

            commentService.save(c1);
            commentService.save(c2);
            commentService.save(c3);


            System.out.println("Comentarios añadidos correctamente.");
        } else {
            System.out.println("No se pudieron crear comentarios por falta de productos o usuarios.");
        }


    }
}