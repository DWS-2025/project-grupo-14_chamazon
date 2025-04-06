package es.urjc.chamazon.restControllers;

import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    @Autowired
    private CommentService commentService;

    // GET ALL COMMENTS
    @GetMapping("/")
    public List<CommentDTO> getAllComments() {
        return commentService.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO comment = commentService.findById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // CREATE NEW COMMENT
    @PostMapping("/")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO savedComment = commentService.save(commentDTO);
        return ResponseEntity.ok(savedComment);
    }

    // UPDATE COMMENT
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        CommentDTO existing = commentService.findById(id);
        if (existing != null) {
            commentDTO.setId(id);
            return ResponseEntity.ok(commentService.save(commentDTO));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE COMMENT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        CommentDTO comment = commentService.findById(id);
        if (comment != null) {
            commentService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

