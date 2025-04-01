package es.urjc.chamazon.dto;
import es.urjc.chamazon.models.Product;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Collection;


@Mapper(componentModel = "spring")
@Component

public interface ProductMapper {
    ProductDTO toDTO(Product product);
    Product toProduct(ProductDTO productDTO);
    Collection<ProductDTO> toDTOs(Collection<Product> products);
    Collection<Product> toProduct(Collection<ProductDTO> productDTOs);
}