package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String getAllComments(Model model) {
        model.addAttribute("comments", commentService.findAll());
        return "comment/addNewComment";
    }

    @GetMapping("/{id}")
    public String getCommentById(@PathVariable Long id, Model model) {
        model.addAttribute("comment", commentService.findById(id).orElse(null));
        return "comment/addNewComment";
    }

    @PostMapping
    public String createComment(@ModelAttribute Comment comment) {
        commentService.save(comment);
        return "redirect:/comments";
    }

    @GetMapping("/edit/{id}")
    public String getEditCommentPage(@PathVariable Long id, Model model) {
        model.addAttribute("comment", commentService.findById(id).orElse(null));
        return "comment/editComment";
    }

    @PostMapping("/edit/{id}")
    public String updateComment(@PathVariable Long id, @ModelAttribute Comment commentDetails) {
        Comment comment = commentService.findById(id).orElse(null);
        if (comment != null) {
            comment.setComment(commentDetails.getComment());
            comment.setRating(commentDetails.getRating());
            comment.setUser(commentDetails.getUser());
            comment.setProduct(commentDetails.getProduct());
            commentService.save(comment);
        }
        return "redirect:/comments";
    }

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return "redirect:/comments";
    }
}
