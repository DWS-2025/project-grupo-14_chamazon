package es.urjc.chamazon.restControllers;

import es.urjc.chamazon.configurations.SecurityUtils;
import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.dto.CommentMapper;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.services.CommentService;
import es.urjc.chamazon.services.UserService;
import es.urjc.chamazon.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // 1. GET ALL COMMENTS
    @GetMapping
    public List<CommentDTO> getAllComments() {
        return commentService.findAll();
    }

    // 2. GET COMMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO comment = commentService.findById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. CREATE COMMENT (like in POST /commentView/add)
    @PostMapping("/create")
    public ResponseEntity<String> createComment(
            @RequestParam String commentTxt,
            @RequestParam int rating,
            @RequestParam Long userId,
            @RequestParam Long productId,
            Authentication auth) {

        boolean created = commentService.createComment(commentTxt, rating, userId, productId);

        if (created) {
            boolean isAdmin = SecurityUtils.isAdmin();
            String redirectUrl = isAdmin ?
                    "/commentView/commentList?productId=" + productId :
                    "/products/" + productId;
            return ResponseEntity.ok(redirectUrl);
        } else {
            return ResponseEntity.badRequest().body("No se pudo crear el comentario");
        }
    }

    // 4. UPDATE COMMENT (like POST /edit/{id})
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateComment(
            @PathVariable Long id,
            @RequestParam("comment") String commentTxt,
            @RequestParam int rating,
            Authentication auth) {

        CommentDTO commentDTO = commentService.findById(id);

        if (commentDTO != null) {
            commentDTO.setComment(commentTxt);
            commentDTO.setRating(rating);
            commentService.save(commentDTO);

            boolean isAdmin = SecurityUtils.isAdmin();
            String redirectUrl = isAdmin ?
                    "/commentView/commentList?productId=" + commentDTO.getProduct().id() :
                    "/products/" + commentDTO.getProduct().id();

            return ResponseEntity.ok(redirectUrl);
        }

        return ResponseEntity.notFound().build();
    }

    // 5. DELETE COMMENT (like POST /delete/{id})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, Authentication auth) {
        CommentDTO commentDTO = commentService.findById(id);
        if (commentDTO != null) {
            commentService.deleteById(id);

            boolean isAdmin = SecurityUtils.isAdmin();
            String redirectUrl = isAdmin ?
                    "/commentView/commentList?productId=" + commentDTO.getProduct().id() :
                    "/products/" + commentDTO.getProduct().id();

            return ResponseEntity.ok(redirectUrl);
        }

        return ResponseEntity.notFound().build();
    }

    // 6. GET COMMENTS BY PRODUCT (paginated)
    @GetMapping("/product/{productId}")
    public Page<CommentDTO> getCommentsByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentService.findByProductId(productId, pageable);
        return commentPage.map(commentMapper::toDTO);
    }

    // 7. GET COMMENT LIST (all or by productId, like commentList view)
    @GetMapping("/list")
    public ResponseEntity<List<CommentDTO>> getCommentList(@RequestParam(required = false) Long productId) {
        if (productId != null) {
            return ResponseEntity.ok(commentService.findByProductId(productId));
        } else {
            return ResponseEntity.ok(List.of()); // lista vacía si no hay productId
        }
    }
}
