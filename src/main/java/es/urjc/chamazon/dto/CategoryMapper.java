package es.urjc.chamazon.dto;
import es.urjc.chamazon.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper(componentModel = "spring", uses = {ProductMapper.class})
@Component
public interface CategoryMapper {
    CategoryDTOExtended toDTO(Category category);
    Category toCategoryFromExtendedDTO(CategoryDTOExtended extendedCategoryDTO);
    Category toCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> toDtoList(List<Category> categoryList);
    List<CategoryDTOExtended> toDTOs(List<Category> categories);
    List<Category> toCategories(List<CategoryDTO> categoriesDTO);
    List<Category> toCategoriesFromExtended(List<CategoryDTOExtended> categoryDTOExtended);

}
