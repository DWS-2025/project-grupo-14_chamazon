package es.urjc.chamazon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class ChamazonApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChamazonApplication.class, args);

    }


    /*Create unique chamazon object*/
    /*Crear al unico objeto chamazon*/
/*
    @Bean
    public ChamazonBDD chamazonBDD(){
        Object product = new Object();
        List<Object> products = new ArrayList<>();
        ShoppingCar shoppingCar = new ShoppingCar(1,products);

        ChamazonBDD chamazonBDD = new ChamazonBDD();
        chamazonBDD.addShoppingCar(shoppingCar);

        return chamazonBDD;
    }

*/


}
