package es.urjc.chamazon.restControllers;


import es.urjc.chamazon.dto.CategoryMapper;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("api/categories/")
public class CategoryRestController {

    @Autowired
    CategoryService categoryService;

}
