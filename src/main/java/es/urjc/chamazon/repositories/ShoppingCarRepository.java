package es.urjc.chamazon.repositories;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.ShoppingCar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ShoppingCarRepository extends JpaRepository<ShoppingCar, Long> {
/*    List<ShoppingCar> findByProducts(Product product);
    List<ShoppingCar> findByDateSold(Date sold);*/
}
