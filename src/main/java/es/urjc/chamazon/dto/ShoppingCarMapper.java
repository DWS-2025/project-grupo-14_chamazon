package es.urjc.chamazon.dto;

import es.urjc.chamazon.models.ShoppingCar;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShoppingCarMapper {

    ShoppingCarDTO toDTO(ShoppingCar shoppingCar);

    List<ShoppingCarDTO> toDTOs(List<ShoppingCar> shoppingCars);

    ShoppingCar toDomain(ShoppingCarDTO shoppingCarDTO);
}
