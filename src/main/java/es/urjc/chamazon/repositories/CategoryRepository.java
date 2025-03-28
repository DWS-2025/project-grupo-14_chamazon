package es.urjc.chamazon.repositories;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT p FROM Category c JOIN c.productList p WHERE c.id = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId);
}
