package es.urjc.chamazon.controllersREST;


import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    ProductService productService;

}
