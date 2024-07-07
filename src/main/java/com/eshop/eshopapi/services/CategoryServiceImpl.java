package com.eshop.eshopapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.eshop.eshopapi.dao.Category;
import com.eshop.eshopapi.models.CategoryDto;
import com.eshop.eshopapi.models.CategoryTreeMap;
import com.eshop.eshopapi.repository.CategoryRepository;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    private final MongoTemplate mongoTemplate;

    @Override
    public Category saveCategory(CategoryDto categoryDto) {
        List<Category> existingCategory = new ArrayList<>();
        Category category = null;
        Query query = new Query();
            query.addCriteria(Criteria.where("categoryId").is(categoryDto.getCategoryId()));
            existingCategory = mongoTemplate.find(query, Category.class);
            category = existingCategory.isEmpty() ? null : existingCategory.get(0);
        if(category!=null) {
            category.setCategoryId(categoryDto.getCategoryId());
            category.setCategoryName(categoryDto.getCategoryName());
            category.setDisplayImage(categoryDto.getDisplayImage());
            category.setParentCategory(categoryDto.getParentId());
        } else {
            category = new Category();
            category.setCategoryId(categoryDto.getCategoryId());
            category.setCategoryName(categoryDto.getCategoryName());
            category.setDisplayImage(categoryDto.getDisplayImage());
            category.setParentCategory(categoryDto.getParentId());
        }
         return categoryRepository.save(category);
    }

    @Override
    public List<CategoryDto> getCategories(String categoryId) {

        List<Category> allCategories = new ArrayList<Category>();

        if(categoryId.equals("all")) {
            allCategories = categoryRepository.findAll();
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("categoryId").is(categoryId));
            allCategories = mongoTemplate.find(query, Category.class);
        }

        List<CategoryDto> categoryDtos = allCategories.stream()
            .map(c -> new CategoryDto(c.getCategoryId(), c.getCategoryName(), c.getParentCategory(), c.getIsParent(), c.getIsChild(), c.getDisplayImage()))
            .toList();

        return categoryDtos;
    }

    @Override
    public List<CategoryTreeMap> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
       
        List<CategoryTreeMap> categoryTreeMaps = new ArrayList<>();
        allCategories.stream().forEach(c -> {
            if(c.getParentCategory()==null) {
                CategoryTreeMap categoryMap = new CategoryTreeMap();
                categoryMap.setId(c.getCategoryId());
                categoryMap.setName(c.getCategoryName());
                categoryMap.setChildren(getChildren(allCategories, c.getCategoryId()));
                categoryTreeMaps.add(categoryMap);
            }
            
        });
        return categoryTreeMaps;
    }
    
    public List<CategoryTreeMap> getChildren(List<Category> allCategories, String parentId) {
        List<CategoryTreeMap> childCategories = new ArrayList<>();
        List<Category> childCategoryList = allCategories.stream().filter(cat -> cat.getParentCategory()!=null && cat.getParentCategory().equals(parentId)).toList();
        childCategoryList.forEach(c -> {
            CategoryTreeMap childCategory = new CategoryTreeMap();
            childCategory.setId(c.getCategoryId());
            childCategory.setName(c.getCategoryName());
            childCategory.setChildren(getChildren(allCategories, c.getCategoryId()));
            childCategories.add(childCategory);
        });

        return childCategories;
    }

    @Override
    public String deleteCategory(String categoryId) {
        Optional<Category> category = Optional.empty();
        category = categoryRepository.findByCategoryId(categoryId);
        if(!category.isEmpty()) {
            categoryRepository.delete(category.get());
            return "Category deleted successfully.";
        }
        else {
            return "Category doesn't exists.";
        }
    }

    
}
