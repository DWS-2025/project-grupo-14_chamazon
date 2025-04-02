package es.urjc.chamazon.dto;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.ShoppingCar;

import java.util.List;

public record ProductDTO(
    Long id,
    String name,
    Float price,
    String description,
    Float rating,
    List<Category> categoryList,
    List<ShoppingCar> shoppingCarList,
    boolean hasImg
){
}
    
