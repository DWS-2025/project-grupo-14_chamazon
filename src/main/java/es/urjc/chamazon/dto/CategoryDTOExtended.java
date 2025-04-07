package es.urjc.chamazon.dto;

import es.urjc.chamazon.models.Product;

import java.util.List;

public record CategoryDTOExtended(
        Long id,
        String name,
        String description,
        List<ProductDTO> productDTOList){
}
