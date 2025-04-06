package es.urjc.chamazon.dto;
import es.urjc.chamazon.models.Product;
import org.mapstruct.Mapper;
import java.util.Collection;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    Product toProduct(ProductDTO productDTO);
    Collection<ProductDTO> toDTOs(Collection<Product> products);
    Collection<Product> toProduct(Collection<ProductDTO> productDTOs);
}