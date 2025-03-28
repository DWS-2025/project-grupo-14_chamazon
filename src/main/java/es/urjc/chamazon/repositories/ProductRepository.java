package es.urjc.chamazon.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.chamazon.models.Product;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByName(String name);
    
    Optional<Product> findByRating(Float rating);
    

}
