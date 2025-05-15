package es.urjc.chamazon.services;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.ProductDTOExtended;
import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.models.Product;

import es.urjc.chamazon.dto.ShoppingCarDTO;
import es.urjc.chamazon.dto.ShoppingCarExtendedDTO;

@Service
public class SanitizationService {

    //---------------------------------PRINCIPAL METHODS FOR SANITIZATION---------------------------------

    // Use jsoup to sanitize inputs forms
    public String sanitizeBasic(String input) {
        if (input == null) {
            return null;
        }

        return Jsoup.clean(input, Safelist.basic());
    }

    
    // Use JSOUP to sanitize inputs forms with none
    public String sanitizeNone(String input) {
        if (input == null) {
            return null;
        }       
        return Jsoup.clean(input, Safelist.none());
    }

    // Jsoup to sanitize custom text using QuillJS - made for comments
    public String sanitizeQuill(String input) {
        if (input == null) {
            return null;
        }

        // Custom whitelist quilljs
        Safelist quillSafelist = new Safelist()
                .addTags("p", "br", "strong", "em", "ul", "ol", "li", "a")
                .addAttributes("a", "href")
                .addProtocols("a", "href", "http", "https")
                .addEnforcedAttribute("a", "target", "_blank");

        return Jsoup.clean(input, quillSafelist);
    }

    // Use jsoup method for imgs
    public String sanitizeImg(String input) {
        if (input == null) {
            return null;
        }

        // Define a custom Safelist for images
        Safelist imgSafelist = new Safelist()
                .addTags("img")
                .addAttributes("img", "src", "alt")
                .addProtocols("img", "src", "http", "https");

        return Jsoup.clean(input, imgSafelist);
    }


    //---------------------------------ENTITIES SANITIZATION---------------------------------

    // Sanitize userdto
    public UserDTO sanitizeUserDTO(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return new UserDTO(
                userDTO.id(),
                sanitizeNone(userDTO.firstName()),
                sanitizeNone(userDTO.surName()),
                sanitizeNone(userDTO.userName()),
                userDTO.password(),
                sanitizeNone(userDTO.email()),
                sanitizeBasic(userDTO.phone()),
                sanitizeBasic(userDTO.address()));
    }

    // Sanitize producdto for addding
    public ProductDTOExtended sanitizeProductDTO(ProductDTOExtended productDTO) {
        if(productDTO == null) {
            return null;
        }
        return new ProductDTOExtended(
                null,
            sanitizeNone(productDTO.name()),
            productDTO.price(),
            sanitizeBasic(productDTO.description()),
            productDTO.rating(),
            sanitizeImg(productDTO.image()),
            productDTO.originalFileName(),
            productDTO.storedFileName(),
            productDTO.filePath(),
            productDTO.commentDTOList(),
            productDTO.categoryDTOList());
    }

    // Sanitize product for updating
    public Product sanitizePoduct (Product product) {
        if(product == null) {
            return null;
        }
        product.setId(product.getId());
        product.setName(sanitizeNone(product.getName()));
        product.setPrice(product.getPrice());
        product.setDescription(sanitizeQuill(product.getDescription()));
        product.setRating(product.getRating());
        product.setImage(sanitizeImg(product.getImage()));
            
        return product;
    }


    // Sanitize commentdto
    public CommentDTO sanitizeCommentDTO(CommentDTO commentDTO) {
        if (commentDTO == null) {
            return null;
        }

        return new CommentDTO(
        commentDTO.getId(),
        sanitizeQuill(commentDTO.getComment()), 
        commentDTO.getRating(),
        commentDTO.getUser(),
        commentDTO.getProduct(),
        commentDTO.isCanEditOrDelete());
    }

}
