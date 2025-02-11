package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class DataBaseInitializer {
    @Autowired
    private CategoryService categoryService;

    @PostConstruct
    public void init() throws IOException {
        Category category1 = new Category(1, "ropa");
        Category category2 = new Category(2, "joyeria");


        /*camisaDeVerano.setPurchases(List.of(purchase1,purchase2));
        patanlonVaquero.setPurchases(List.of(purchase1,purchase2));
        zapatoDeCuero.setPurchases(List.of(purchase1,purchase2));
        faldaDeTablas.setPurchases(List.of(purchase1,purchase2));
        gorroDeLana.setPurchases(List.of(purchase1,purchase2));
        chaquetaDeCuero.setPurchases(List.of(purchase1,purchase2));
        bufandaDeRayas.setPurchases(List.of(purchase1,purchase2));
        vestidoElegante.setPurchases(List.of(purchase1,purchase2));
        calcetinesDeportivos.setPurchases(List.of(purchase1,purchase2));*/


        category1.getProduct(1);
        category1.getProduct(2);
        /*
        category2.getProducts().add(camisaDeVerano);
        category2.getProducts().add(chaquetaDeCuero);

        productService.save(camisaDeVerano);
        productService.save(patanlonVaquero);
        productService.save(zapatoDeCuero);
        productService.save(faldaDeTablas);
        productService.save(gorroDeLana);
        productService.save(chaquetaDeCuero);
        productService.save(bufandaDeRayas);
        productService.save(vestidoElegante);
        productService.save(calcetinesDeportivos);

        categoryService.save(purchase1);
        categoryService.save(purchase2);
        */
    }
}
