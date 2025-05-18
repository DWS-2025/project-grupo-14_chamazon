package es.urjc.chamazon.dto;

public record CommentDTOSimple(
        Long id,
        String comment,
        int rating,
        SimpletUserDTO user
        ){
}