package es.urjc.chamazon.dto;

import java.util.List;

public record ProductDTOExtended(
        Long id,
        String name,
        Float price,
        String description,
        Float rating,
        String image,
        String originalFileName,  // Nuevo campo
        String storedFileName,    // Nuevo campo
        String filePath,
        List<CommentDTO> commentDTOList,
        List<CategoryDTO> categoryDTOList
){
}