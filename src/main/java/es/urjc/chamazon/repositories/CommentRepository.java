package es.urjc.chamazon.repositories;

import es.urjc.chamazon.models.Product;

import  es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository  extends JpaRepository<Comment, Long> {

    Optional <List <Comment>> findByProduct_Id(Long productId); //Method that has to be called in the service to obtain the comments of a product

    Page <Comment> findByProductId(Long productId, Pageable pageable);
    void deleteByUser(User user);

    @Query("SELECT c FROM Comment c WHERE c.user is null ")
    Optional<List<Comment>> findByUserIsNull();
}
