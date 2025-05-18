package es.urjc.chamazon.dto;

import es.urjc.chamazon.models.Comment;

public record CommentDTOSimple(
        Long id,
        String comment,
        int rating,
        CommentUserDTO user
        ){
}