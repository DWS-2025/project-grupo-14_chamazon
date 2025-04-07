package es.urjc.chamazon.dto;


import org.mapstruct.Mapper;
import es.urjc.chamazon.models.Comment;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface CommentMapper {

    CommentDTO toDTO(Comment comment);
    Comment toDomain(CommentDTO commentDTO);
    List <CommentDTO> toDTOs(List<Comment> comments);
    List<Comment> toDomains(List<CommentDTO> commentDTOs);

}
