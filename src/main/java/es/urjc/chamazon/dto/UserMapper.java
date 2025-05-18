package es.urjc.chamazon.dto;

import es.urjc.chamazon.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserIndividualDTO toIndividualDTO(User user);
    UserDTO toDTO(User user);
    User toUser(UserDTO userDTO);
    List<UserDTO> toDTOs(List<User> users);
    List<User> toUsers(List<UserDTO> usersDTOs);
    @Mapping(target = "commentDTOList", source = "comments")
    @Mapping(target = "surName", source = "surName")
    UserDTOExtended toDTOExtended(User user);
}
