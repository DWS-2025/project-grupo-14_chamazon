package es.urjc.chamazon.repositories;

import  es.urjc.chamazon.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<Comment, Long> {

}
