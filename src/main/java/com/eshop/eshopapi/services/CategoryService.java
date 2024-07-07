package com.eshop.eshopapi.services;

import java.util.List;

import com.eshop.eshopapi.dao.Category;
import com.eshop.eshopapi.models.CategoryDto;
import com.eshop.eshopapi.models.CategoryTreeMap;

public interface CategoryService {
    public Category saveCategory(CategoryDto categoryDto);
    public List<CategoryDto> getCategories(String categoryName);
    public List<CategoryTreeMap> getAllCategories();
    public String deleteCategory(String categoryId);
}
