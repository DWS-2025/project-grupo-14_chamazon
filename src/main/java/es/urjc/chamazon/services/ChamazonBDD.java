package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.dto.CommentMapper;
import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.repositories.CategoryRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChamazonBDD{

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ShoppingCarService shoppingCarService;
    private final CommentService commentService;

    public ChamazonBDD(CategoryService categoryService, UserService userService,
                       ProductService productService, ShoppingCarService shoppingCarService, CommentService commentService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.commentService = commentService;
        this.shoppingCarService = shoppingCarService;
    }

    @PostConstruct
    public void init() {



        Category electronics = new Category("Electronics", "Electronic devices");
        Category clothing = new Category("Clothing", "Fashion items");
        Category books = new Category("Books", "A collection of books");
        Category travel = new Category("Travel", "Travel gear");


        Product laptop = new Product("Laptop Premium", 999.99F,
                "High performance laptop with 16GB RAM",
                1F);

        Product smartphone = new Product("Smartphone X", 699.99F,
                "Latest model with AMOLED display",
                2F);

        Product tshirt = new Product("Cotton T-Shirt", 19.99F, "Cotton T-Shirt",
                3F);

        Product novel = new Product("Best Seller Novel", 14.99F,
                "The most popular novel this year",
                4F);

        Product backpack = new Product("Travel Backpack", 49.99F,
                "Durable backpack for travelers",
                4.3F);


        Product raton = new Product("Raton", 9.99F,
                "Raton para el ordenador",
                5F);

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


        // Product in many categories:
        // Optional: Product in multiple categories 
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



        userService.saveUser(new User( "PepeAdmin", "Pepe", "Montero", passwordEncoder.encode("123") , "pepe@mail.com", "098", "q", "ADMIN"));
        userService.saveUser(new User( "User2", "Maria", "Carrera ", passwordEncoder.encode("321"),  "maria@mail.com", "100", "p", "USER"));
        userService.saveUser(new User( "User3", "Fernando", "Alonso", passwordEncoder.encode("333"), "fernando@mail.com", "200", "r", "USER"));

        //shoppingCarService.addProductToUserShoppingCar(1L,1L);


        shoppingCarService.addProductToUserShoppingCar(2L,1L);
        shoppingCarService.addProductToUserShoppingCar(3L, 1L);
        shoppingCarService.addProductToUserShoppingCar(4L, 1L);

        System.out.println("Categorías creadas correctamente.");

        User user1 = userService.getUserById(1L);
        User user2 = userService.getUserById(2L);
        User user3 = userService.getUserById(3L);

        Product product1 = productService.findById(1L).orElse(null);
        Product product2 = productService.findById(2L).orElse(null);
        Product product3 = productService.findById(3L).orElse(null);

        if (user1 != null && user2 != null && product1 != null && product2 != null && product3 != null) {

            CommentDTO c1 = commentMapper.toDTO(new Comment("Me encanta este producto, ¡lo uso cada dia!", 5, user1, product1)); //Is PepeAdmin
            CommentDTO c2 = commentMapper.toDTO(new Comment("Podria ser mejor, pero mola", 3, user2, product2)); //Is User2
            CommentDTO c3 = commentMapper.toDTO(new Comment("Excelente relacion calidad-precio", 4, user1, product3)); //Is PepeAdmin
            CommentDTO c4 = commentMapper.toDTO(new Comment("No me gusta nada, es muy lento", 1, user3, product1)); //Is User3

            commentService.save(c1);
            commentService.save(c2);
            commentService.save(c3);
            commentService.save(c4);

            // Extra comments to prove the functionality of pagination of Laptop Premium product (now we have 11 comments more)
            commentService.save(commentMapper.toDTO(new Comment("Muy recomendable, va como un tiro", 5, user2, product1)));
            commentService.save(commentMapper.toDTO(new Comment("Un poco caro, pero vale la pena", 4, user1, product1)));
            commentService.save(commentMapper.toDTO(new Comment("No me conevence del todo, esperaba algo mejor", 2, user2, product1)));
            commentService.save(commentMapper.toDTO(new Comment("Diseño elegante y buen rendimiento, la booooomba", 5, user1, product1)));
            commentService.save(commentMapper.toDTO(new Comment("Lo uso para trabajar y va genial", 5, user2, product1)));
            commentService.save(commentMapper.toDTO(new Comment("Problemas con la bateria a los 2 meses", 3, user1, product1)));
            commentService.save(commentMapper.toDTO(new Comment("No he tenido ningun problema hasta ahora", 4, user2, product1)));
            commentService.save(commentMapper.toDTO(new Comment("Perfecto para estudiar y jugar", 5, user1, product1)));
            commentService.save(commentMapper.toDTO(new Comment("Igual de malo que el Atletico de Madrid, aparenta mucho pero es una mierda", 1, user1, product1)));
            commentService.save(commentMapper.toDTO(new Comment("Como el Real Madrid, nunca muere, aguanta hasta el final", 5, user2, product1)));
            commentService.save(commentMapper.toDTO(new Comment("El Aston Martin es muy lento, pero me pagan millones, ¡lo uso cada dia!", 5, user3, product1)));



            System.out.println("Comentarios añadidos correctamente.");
        } else {
            System.out.println("No se pudieron crear comentarios por falta de productos o usuarios.");
        }


    }
}