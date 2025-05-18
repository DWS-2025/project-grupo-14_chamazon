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
    @PostMapping("/")
    public ResponseEntity<String> createComment(
            @RequestBody CommentDTO commentDTO,
            Authentication authentication) {

        String username = authentication.getName();
        var user = userService.findByUserName(username);

        if (user == null || commentDTO.getProduct() == null) {
            return ResponseEntity.badRequest().body("Faltan datos de usuario o producto");
        }

        boolean created = commentService.createComment(
                commentDTO.getComment(),
                commentDTO.getRating(),
                user.get().id(),
                commentDTO.getProduct().id()
        );

        if (created) {
            boolean isAdmin = SecurityUtils.isAdmin();
            String redirectUrl = isAdmin ?
                    "/commentView/commentList?productId=" + commentDTO.getProduct().id() :
                    "/products/" + commentDTO.getProduct().id();

            return ResponseEntity.ok(redirectUrl);
        } else {
            return ResponseEntity.badRequest().body("No se pudo crear el comentario");
        }
    }



    // 4. UPDATE COMMENT (like POST /edit/{id})
    @PutMapping("/{id}")
    public ResponseEntity<String> updateComment(
            @PathVariable Long id,
            @RequestBody CommentDTO updatedCommentDTO,
            Authentication auth) {

        CommentDTO existingCommentDTO = commentService.findById(id);

        if (existingCommentDTO == null) {
            return ResponseEntity.notFound().build();
        }

        String username = auth.getName();
        var user = userService.findByUserName(username);

        if (!existingCommentDTO.getUser().id().equals(user.get()) && !SecurityUtils.isAdmin()) {
            return ResponseEntity.status(403).body("No tienes permiso para editar este comentario");
        }

        existingCommentDTO.setComment(updatedCommentDTO.getComment());
        existingCommentDTO.setRating(updatedCommentDTO.getRating());
        existingCommentDTO.setProduct(updatedCommentDTO.getProduct());
        commentService.save(existingCommentDTO);

        String redirectUrl = "/products/" + existingCommentDTO.getProduct().id();
        return ResponseEntity.ok(redirectUrl);
    }


    // 5. DELETE COMMENT (like POST /delete/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, Authentication auth) {
        CommentDTO commentDTO = commentService.findById(id);

        if (commentDTO == null) {
            return ResponseEntity.notFound().build();
        }

        String username = auth.getName();
        var user = userService.findByUserName(username);

        if (!commentDTO.getUser().id().equals(user.get().id() ) && !SecurityUtils.isAdmin()) {
            return ResponseEntity.status(403).body("No tienes permiso para eliminar este comentario");
        }

        commentService.deleteById(id);
        String redirectUrl = "/products/" + commentDTO.getProduct().id();
        return ResponseEntity.ok(redirectUrl);
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
}
