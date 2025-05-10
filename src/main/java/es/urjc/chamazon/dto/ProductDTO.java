package es.urjc.chamazon.dto;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.ShoppingCar;

import java.util.List;

public record ProductDTO(
        Long id,
        String name,
        Float price,
        String description,
        String image,
        String originalFileName,  // Nuevo campo
        String storedFileName,    // Nuevo campo
        String filePath
){
}
    
