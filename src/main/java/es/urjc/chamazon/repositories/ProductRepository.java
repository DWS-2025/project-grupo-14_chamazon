package es.urjc.chamazon.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.urjc.chamazon.models.Product;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);
    Optional<Product> findByRating(Float rating);

    @Query("SELECT p FROM Product p JOIN p.categoryList c WHERE c.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    List <Product> findByPrice(Float price);

    List <Product> findByPriceBetween(Float minPrice, Float maxPrice);

    @Query("SELECT p FROM Product p JOIN p.categoryList c WHERE c.id = :categoryId AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByCategoryIdAndPriceBetween(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice);

    List<Product> findByRatingGreaterThanEqual(Float minRating);

    @Query("SELECT p FROM Product p JOIN p.categoryList c WHERE c.id = :categoryId AND p.rating >= :minRating")
    List<Product> findByCategoryIdAndRatingGreaterThanEqual(@Param("categoryId") Long categoryId, @Param("minRating") Float minRating);

}