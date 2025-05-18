package es.urjc.chamazon.dto;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.ShoppingCar;

import java.util.List;

public record FilteredDto(
         Long categoryId,
         Float minPrice,
         Float maxPrice,
         Float rating
){
}

