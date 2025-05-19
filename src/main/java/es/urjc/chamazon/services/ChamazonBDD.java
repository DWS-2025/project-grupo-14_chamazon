package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.dto.CommentMapper;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

@Service
public class ChamazonBDD{

    @Autowired
    private CommentMapper commentMapper;


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

        //Safe secondary entity
        categoryService.save(electronics);
        categoryService.save(clothing);
        categoryService.save(books);
        categoryService.save(travel);


        Product[] products = new Product[]{
                new Product("Laptop Premium", 999.99F, "High performance laptop with 16GB RAM", 1F),
                new Product("Smartphone X", 699.99F, "Latest model with AMOLED display", 2F),
                new Product("Cotton T-Shirt", 19.99F, "Cotton T-Shirt", 3F),
                new Product("Best Seller Novel", 14.99F, "The most popular novel this year", 4F),
                new Product("Travel Backpack", 49.99F, "Durable backpack for travelers", 4.3F),
                new Product("Mouse", 9.99F, "Raton para el ordenador", 5F),
                new Product("Smart Watch", 199.99F, "Fitness tracker with notifications", 4.0F),
                new Product("Noise Cancelling Headphones", 149.99F, "Great for travel and work", 4.5F),
                new Product("E-book Reader", 89.99F, "Read books anywhere, any time", 4.1F),
                new Product("Running Shoes", 79.99F, "Perfect for daily runs and casual use", 4.2F),
                new Product("Portable Charger", 29.99F, "Charge your devices on the go", 3.8F),
                new Product("Bluetooth Speaker", 39.99F, "Loud and compact speaker", 4.0F),
                new Product("Leather Jacket", 129.99F, "Stylish and warm", 4.6F),
                new Product("Fantasy Book Saga", 59.99F, "Complete 5-book fantasy series", 4.7F)
        };


        // Asignar categorias e imagenes
        products[0].getCategoryList().add(electronics);
        products[1].getCategoryList().add(electronics);
        products[2].getCategoryList().add(clothing);
        products[3].getCategoryList().add(books);
        products[4].getCategoryList().add(travel);
        products[5].getCategoryList().add(electronics);
        products[6].getCategoryList().add(clothing);
        products[6].getCategoryList().add(electronics);
        products[7].getCategoryList().add(electronics);
        products[7].getCategoryList().add(travel);
        products[8].getCategoryList().add(electronics);
        products[8].getCategoryList().add(books);
        products[9].getCategoryList().add(clothing);
        products[10].getCategoryList().add(electronics);
        products[10].getCategoryList().add(travel);
        products[11].getCategoryList().add(electronics);
        products[12].getCategoryList().add(clothing);
        products[13].getCategoryList().add(books);


        for (Product product : products) {
            try {
                productService.save(product);
            } catch (Exception e) {
                System.out.println("Error saving product: " + e.getMessage());
            }
        }





        userService.saveUser(new User( "PepeAdmin", "Pepe", "Montero", "123" , "pepe@mail.com", "098", "q", "ADMIN"));
        userService.saveUser(new User( "User2", "Maria", "Carrera ", "222",  "maria@mail.com", "100", "p", "USER"));
        userService.saveUser(new User( "User3", "Fernando", "Alonso", "333", "fernando@mail.com", "200", "r", "USER"));
        userService.saveUser(new User("User4", "Laura", "Martínez", "444", "laura@mail.com", "201", "s", "USER"));
        userService.saveUser(new User("User5", "Carlos", "Ruiz", "555", "carlos@mail.com", "202", "t", "USER"));
        userService.saveUser(new User("User6", "Lucía", "Gómez", "666", "lucia@mail.com", "203", "u", "USER"));
        userService.saveUser(new User("User7", "Jorge", "López", "777", "jorge@mail.com", "204", "v", "USER"));
        userService.saveUser(new User("User8", "Marta", "Sánchez", "888", "marta@mail.com", "205", "w", "USER"));
        userService.saveUser(new User("User9", "Raúl", "Pérez", "999", "raul@mail.com", "206", "x", "USER"));
        userService.saveUser(new User("User10", "Ana", "Torres", "000", "ana@mail.com", "207", "y", "USER"));

        for (long userId = 1L; userId <= 10L; userId++) {
            for (long productId = userId; productId <= userId + 2 && productId <= products.length; productId++) {
                shoppingCarService.addProductToUserShoppingCar((Long) productId, (Long)userId);
            }
        }

/*        shoppingCarService.addProductToUserShoppingCar(2L,1L);
        shoppingCarService.addProductToUserShoppingCar(3L, 1L);
        shoppingCarService.addProductToUserShoppingCar(4L, 1L);*/


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