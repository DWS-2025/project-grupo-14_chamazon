package es.urjc.chamazon.dto;

import es.urjc.chamazon.models.ShoppingCar;

import java.util.List;

public record UserDTOExtended (
        Long id,
        String firstName,
        String surname,
        String userName,
        String password,
        String email,
        String phone,
        String address,
        List<ShoppingCarDTO> shoppingCarDTOList,
        List<CommentDTO> commentDTOList){
}
