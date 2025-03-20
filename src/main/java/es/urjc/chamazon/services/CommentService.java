package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService  {

    @Autowired
    private CommentRepository commentRepository;

    //Method to find all comments
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    //Method to find a comment by its id if it exists
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    //Method to save a comment
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    //Method to delete a comment by its id
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

}
