package es.urjc.chamazon.dto;

import es.urjc.chamazon.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toUser(UserDTO userDTO);
    List<UserDTO> toDTOs(List<User> users);
    List<User> toUsers(List<UserDTO> usersDTOs);
}
