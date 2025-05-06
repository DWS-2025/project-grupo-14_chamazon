package es.urjc.chamazon.services;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.CommentDTO;
import es.urjc.chamazon.dto.ShoppingCarDTO;


@Service
public class SanitizationService {
    

    //Use jsoup to sanitize inputs forms
    public String sanitizeBasic(String input) {
        if(input == null) {
            return null;
        }

        return Jsoup.clean(input, Safelist.basic());
    }


    //Use JSOUP to sanitize inputs forms with none
    public String sanitizeNone (String input){
        if(input == null) {
            return null;
        }
        return Jsoup.clean(input, Safelist.none());
    }

    //Jsoup to sanitize custom text using QuillJS

    public String sanitizeQuill(String input) {
        if(input == null) {
            return null;
        }

        // Define a custom Safelist for QuillJS
        Safelist quillSafelist = new Safelist()
                .addTags("p", "br", "strong", "em", "ul", "ol", "li", "a")
                .addAttributes("a", "href")
                .addProtocols("a", "href", "http", "https")
                .addEnforcedAttribute("a", "target", "_blank");

        return Jsoup.clean(input, quillSafelist);
    }

    //Use jsoup method for imgs
    public String sanitizeImg(String input) {
        if(input == null) {
            return null;
        }

        // Define a custom Safelist for images
        Safelist imgSafelist = new Safelist()
                .addTags("img")
                .addAttributes("img", "src", "alt")
                .addProtocols("img", "src", "http", "https");

        return Jsoup.clean(input, imgSafelist);
    }



    public UserDTO sanitizeUserDTO(UserDTO userDTO) {
        if(userDTO == null) {
            return null;
        }

        return new UserDTO(
                userDTO.id(),
                sanitizeNone(userDTO.firstName()),
                sanitizeNone(userDTO.surname()),
                sanitizeNone(userDTO.userName()),
                userDTO.password(), 
                sanitizeNone(userDTO.email()),
                sanitizeBasic(userDTO.phone()),
                sanitizeBasic(userDTO.address())
        );
    }

    public ProductDTO sanitizeProductDTO(ProductDTO productDTO) {
        if(productDTO == null) {
            return null;
        }

        return new ProductDTO(
                productDTO.id(),
                sanitizeNone(productDTO.name()),
                productDTO.price(),
                sanitizeBasic(productDTO.description()),
                sanitizeImg(productDTO.image())
        );
    }
}
