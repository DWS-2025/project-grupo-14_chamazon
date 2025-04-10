package es.urjc.chamazon.controllers;

import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.dto.ProductDTOExtended;
import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.models.Product;
//import es.urjc.chamazon.models.User;
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
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("comment", new Comment());
        return "comment/addNewComment";
    }

    @PostMapping("/add")
    public String addComment(@RequestParam String commentTxt, @RequestParam int rating, @RequestParam Long userId , @RequestParam Long productId) {
        boolean isCreated = commentService.createComment(commentTxt, rating, userId, productId);

        if (isCreated) {

            return "redirect:/commentView/commentList?productId=" + productId;
        } else {
            // Manejar el caso en que el producto no se encuentra
            return "redirect:/commentView/commentList";
        }
    }

    @GetMapping("/commentList")
    public String getCommentList(@RequestParam(required = false) Long productId, Model model) {
        List<ProductDTOExtended> products = (List <ProductDTOExtended>) productService.getProducts();
        model.addAttribute("products", products);

        if (productId != null) {
            List <CommentDTO> comments = commentService.findByProductId(productId);
            model.addAttribute("comments", comments);
        } else {
            model.addAttribute("comments", new ArrayList <CommentDTO>());
        }

        return "comment/commentList";
    }



    //Update Operations: method getEditCommentPage(GET) and updateComment(POST) to update a comment
    @GetMapping("/edit/{id}")
    public String getEditCommentPage(@PathVariable Long id, Model model) {
        CommentDTO commentDTO = commentService.findById(id);

        if (commentDTO != null) {
            model.addAttribute("comment", commentDTO);
            return "comment/editComment";
        } else {
            return "redirect:/commentView/add";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateComment(@PathVariable Long id,
                                @RequestParam("comment") String commentTxt,
                                @RequestParam int rating) {

        CommentDTO commentDTO = commentService.findById(id);

        if (commentDTO != null) {
            commentDTO.setComment(commentTxt);
            commentDTO.setRating(rating);

            commentService.save(commentDTO);

            return "redirect:/commentView/commentList?productId=" + commentDTO.getProduct().id();
        }

        return "redirect:/commentView/commentList";
    }



    //Delete Operation: method deleteComment(POST) to delete a comment (delete with a button in the comment view)
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        CommentDTO commentDTO = commentService.findById(id);

        if (commentDTO != null) {
            Long productId = commentDTO.getProduct().id();
            commentService.deleteById(id);
            return "redirect:/commentView/commentList?productId=" + productId;
        }
        return "redirect:/commentView/commentList";
    }

}

