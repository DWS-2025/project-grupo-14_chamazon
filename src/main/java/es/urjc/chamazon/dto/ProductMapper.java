package es.urjc.chamazon.dto;
import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {
    @Mapping(source = "categoryList", target = "categoryDTOList")
    @Mapping(target = "image", expression = "java(product.getImageFile() != null ? \"/products/\" + product.getId() + \"/image\" : null)")
    ProductDTOExtended toDTOExtended(Product product);
    ProductDTO toDTO(Product product);
    Product toProduct(ProductDTO productDTO);
    Collection<ProductDTO> toDTOs(Collection<Product> products);
    Collection<Product> toProduct(Collection<ProductDTO> productDTOs);
    Collection<ProductDTOExtended> toDTOsExtended(Collection<Product> products);
    Product toProductFromExtended(ProductDTOExtended productDTOExtended);
}
