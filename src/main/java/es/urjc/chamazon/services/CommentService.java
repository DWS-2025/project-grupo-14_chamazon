package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.dto.CommentMapper;
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

    @Autowired
    private CommentMapper commentMapper;

    //Method to find all comments converted to DTOs
    public List <CommentDTO> findAll() {

        return commentMapper.toDTOs(commentRepository.findAll());
    }

    //Method to find a comment by its id and convert it to DTO
    public CommentDTO findById(Long id) {

        return commentRepository.findById(id).map(commentMapper::toDTO).orElse(null);

    }

    //Method to save a comment (it receives a DTO and converts it to a domain object)
    public CommentDTO save(CommentDTO commentDTO) {
        Comment comment = commentMapper.toDomain(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDTO(savedComment);
    }

    //Method to delete a comment by its id
    public void deleteById(Long id) {

        commentRepository.deleteById(id);
    }

    //Method to find all comments by product id and convert them to DTOs
    public List<CommentDTO> findByProductId(Long productId) {
        return commentRepository.findByProduct_Id(productId)
                .map(commentMapper::toDTOs)
                .orElse(new ArrayList<>());
    }


    public boolean createComment(String commentTxt, int rating, Long userId, Long productId) {
        Optional<Product> optionalProduct = productService.findById(productId);
        User user = userService.getUserById(userId);

        if (optionalProduct.isPresent() && user != null) {
            Product product = optionalProduct.get();

            Comment comment = new Comment(commentTxt, rating, user, product);
            commentRepository.save(comment);
            return true;
        }
        return false;
    }
}

