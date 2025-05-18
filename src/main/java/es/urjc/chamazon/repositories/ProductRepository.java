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


    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.commentList WHERE p.id = :id")
    Optional <Product> findByIdWithComments(@Param("id") Long id);


@Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.categoryList c " +
       "WHERE (:categoryId IS NULL OR c.id = :categoryId) " +
       "AND (:minPrice IS NULL OR :maxPrice IS NULL OR p.price BETWEEN :minPrice AND :maxPrice) " +
       "AND (:rating IS NULL OR p.rating >= :rating)")
List<Product> findByFilters(
        @Param("categoryId") Long categoryId,
        @Param("minPrice") Float minPrice,
        @Param("maxPrice") Float maxPrice,
        @Param("rating") Float rating);

}