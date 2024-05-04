package com.project.bookcycle.service;


import com.project.bookcycle.dto.CategoryDTO;
import com.project.bookcycle.model.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(long id);
}
