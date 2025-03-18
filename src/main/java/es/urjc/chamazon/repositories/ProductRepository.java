package es.urjc.chamazon.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.chamazon.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
