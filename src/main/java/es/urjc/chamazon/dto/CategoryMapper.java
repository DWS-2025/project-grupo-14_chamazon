package es.urjc.chamazon.dto;
import es.urjc.chamazon.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper(componentModel = "spring", uses = {ProductMapper.class})
@Component
public interface CategoryMapper {
    @Mapping(source = "productList", target = "productDTOList")
    CategoryDTOExtended toDTO(Category category);
    Category toCategory(CategoryDTO categoryDTO);
    List<CategoryDTOExtended> toDTOs(List<Category> categories);
    List<Category> toCategories(List<CategoryDTO> categoriesDTO);
}
