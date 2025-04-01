package es.urjc.chamazon.controllers;

import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.services.CommentService;
import es.urjc.chamazon.services.ProductService;
import es.urjc.chamazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

//CRUD operations for comments: Create, Read, Update, Delete

@Controller
@RequestMapping("/commentView") //This part of the path to access the other methods
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;


    //Create Operations: method addCommentForm(GET) and addComment(POST) to add a new comment
    @GetMapping("/add")
    public String addCommentForm(@RequestParam("productId") Long productId, Model model) {
        Optional<Product> product = productService.findById(productId);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
        } else {
            model.addAttribute("errorMessage", "Producto no encontrado");
            return "error/error";
        }
        List<User> users = (List <User>) userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("comment", new Comment());
        return "comment/addNewComment";
    }

    @PostMapping("/add")
    public String addComment(@RequestParam String commentTxt, @RequestParam int rating, @RequestParam("productId") Long productId) {
        Optional<Product> optionalProduct = productService.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            Comment comment = new Comment(commentTxt,rating, null, null); //ptregutar a victor
            //Crear objeto comment con los parámetros
            comment.setProduct(product);
            commentService.save(comment);
            return "redirect:/commentView/commentList?productId=" + productId;
        } else {
            // Manejar el caso en que el producto no se encuentra
            return "redirect:/commentView/commentList";
        }
    }

    @GetMapping("/commentList")
    public String getCommentList(@RequestParam(required = false) Long productId, Model model) {
        List<Product> products = (List <Product>) productService.findAllProducts();
        model.addAttribute("products", products);

        if (productId != null) {
            List<Comment> comments = commentService.findByProductId(productId);
            model.addAttribute("comments", comments);
        } else {
            model.addAttribute("comments", new ArrayList <>());
        }

        return "comment/commentList";
    }



    /*
    @GetMapping("/comments")
    public String getAllComments(Model model) {
        List<Comment> comments = commentService.findAll();
        model.addAttribute("comments", comments);
        return "comment/comments";
    }
    */

    /*
    @GetMapping("/{id}")
    public String getCommentById(@PathVariable Long id, Model model) {
        Optional<Comment> comment = commentService.findById(id);
        if (comment.isPresent()) {
            model.addAttribute("comment", comment.get());
            return "comment/comment";
        } else {
            return "error/error";
        }
    }
    */

    //Update Operations: method getEditCommentPage(GET) and updateComment(POST) to update a comment
    @GetMapping("/edit/{id}")
    public String getEditCommentPage(@PathVariable Optional<Long> id, Model model) {
        if (id.isPresent()) {
            model.addAttribute("comment", commentService.findById(id.get()).orElse(null));
            return "comment/editComment";
        } else {
            return "redirect:/commentView/add";
        }
    }

    @PostMapping("/edit/{id}") //Potential SQL Injection vulnerability según lo que nos ha dicho Raúl , quitar ModelAttribute
    public String updateComment(@PathVariable Long id, @ModelAttribute Comment commentDetails) {
        Comment comment = commentService.findById(id).orElse(null);
        if (comment != null) {
            comment.setComment(commentDetails.getComment());
            comment.setRating(commentDetails.getRating());
            comment.setUser(commentDetails.getUser());
            commentService.save(comment);
            if (comment.getProduct() != null) {
                return "redirect:/commentView/commentList?productId=" + comment.getProduct().getId();
            }
        }
        return "redirect:/commentView/commentList";
    }


    //Delete Operation: method deleteComment(POST) to delete a comment (delete with a button in the comment view)
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        Comment comment = commentService.findById(id).orElse(null);
        if (comment != null) {
            Long productId = comment.getProduct().getId();
            commentService.deleteById(id);
            return "redirect:/commentView/commentList?productId=" + productId;
        }
        return "redirect:/commentView/commentList";
    }

}

