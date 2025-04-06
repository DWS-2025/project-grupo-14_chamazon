package es.urjc.chamazon.dto;
import es.urjc.chamazon.models.Category;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
    Category toCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> toDTOs(List<Category> categories);
    List<Category> toCategories(List<CategoryDTO> categoriesDTO);
    
    }
