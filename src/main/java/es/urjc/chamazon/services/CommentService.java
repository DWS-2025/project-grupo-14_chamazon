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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


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

    @Autowired
    private SanitizationService sanitizationService;

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
        //sanitize comment text
        CommentDTO sanitizedCommentDTO = sanitizationService.sanitizeCommentDTO(commentDTO);
        Comment comment = commentMapper.toDomain(sanitizedCommentDTO);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDTO(savedComment);
    }

    //Method to delete a comment by its id
    public void deleteById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            User user = comment.getUser();
            if (user != null) {
                user.getComments().remove(comment);
                comment.setUser(null);
            }

            Product product = comment.getProduct();
            if (product != null) {
                product.getCommentList().remove(comment);
                comment.setProduct(null);
            }

            commentRepository.deleteById(comment.getId());
        }
    }

    //Method to delete all comments wiht user_id == null
    public void deleteCommentsWithoutUser() {
        Optional<List<Comment>> orphanComments = commentRepository.findByUserIsNull();

        if (orphanComments.isPresent()) {
            for (Comment comment : orphanComments.get()) {
                this.deleteById(comment.getId());
            }
        }
    }

    //Method to find all comments by product id and convert them to DTOs
    public List<CommentDTO> findByProductId(Long productId) {
        return commentRepository.findByProduct_Id(productId)
                .map(commentMapper::toDTOs)
                .orElse(new ArrayList<>());
    }


    public boolean createComment(String commentTxt, int rating, Long userId, Long productId) {
        //sanitize comment text by creating
        String sanitizedCreatedComment = sanitizationService.sanitizeQuill(commentTxt);
        Optional<Product> optionalProduct = productService.findById(productId);
        User user = userService.getUserById(userId);

        if (optionalProduct.isPresent() && user != null) {
            Product product = optionalProduct.get();

            Comment comment = new Comment(sanitizedCreatedComment, rating, user, product);
            user.getComments().add(comment);
            commentRepository.save(comment);
            return true;
        }
        return false;
    }

    //For Pagination
    public Page <Comment> findByProductId(Long productId, Pageable pageable) {
        return commentRepository.findByProductId(productId, pageable);
    }

}

