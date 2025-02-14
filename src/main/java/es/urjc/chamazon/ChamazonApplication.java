package es.urjc.chamazon;

import es.urjc.chamazon.models.ChamazonBDD;
import es.urjc.chamazon.models.ShopingCar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class ChamazonApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChamazonApplication.class, args);

    }


    /*Create unique chamazon object*/
    /*Crear al unico objeto chamazon*/
    @Bean
    public ChamazonBDD chamazonBDD(){
        Object product = new Object();
        List<Object> products = new ArrayList<>();
        ShopingCar shopingCar = new ShopingCar(1,products);

        ChamazonBDD chamazonBDD = new ChamazonBDD();
        chamazonBDD.addShopingCar(shopingCar);

        return chamazonBDD;
    }



}
