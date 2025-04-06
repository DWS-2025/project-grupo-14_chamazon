package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CommentRepository commentRepository;

    //Method to find all comments
    public List <Comment> findAll() {

        return commentRepository.findAll();
    }

    //Method to find a comment by its id if it exists
    public Optional <Comment> findById(Long id) {

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

    public List <Comment> findByProductId(Long productId) {
        // This method will return a list of comments for a given product ID
        return commentRepository.findByProduct_Id(productId).orElse(new ArrayList <>());
        //if the product ID does not exist, it will return an empty list
    }

    public boolean createComment(String commentTxt, int rating, Long userId, Long productId) {

        Optional <Product> optionalProduct = productService.findById(productId);
        User user = userService.getUserById(userId);

        if(optionalProduct.isPresent() && user != null) {
            Product product = optionalProduct.get();

            Comment comment = new Comment(commentTxt, rating, user, product); //Create the comment object with the data from the form

            this.save(comment);
            return true;
        }
        return false;
    }
}

