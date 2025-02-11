package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Service
public class CategoryService {

    private final Map<Integer, Category> categories = new HashMap<>();

    public Collection<Category> getAllCategories() {
        return categories.values();
    }


    public void addCategory(Category category) {
        categories.put(category.getId(), category);
    }
}
