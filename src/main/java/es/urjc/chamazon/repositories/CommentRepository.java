package es.urjc.chamazon.repositories;

import es.urjc.chamazon.models.Product;

import  es.urjc.chamazon.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository  extends JpaRepository<Comment, Long> {

    Optional <List <Comment>> findByProduct_Id(Long productId); //Metodo que tendrá que ser llamado en el servicio para obtener los comentarios de un producto
}
