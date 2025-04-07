package es.urjc.chamazon.dto;

import java.util.List;

public record ProductDTOExtended(
        Long id,
        String name,
        Float price,
        String description,
        Float rating,
        List<CategoryDTO> categoryDTOList
){
}