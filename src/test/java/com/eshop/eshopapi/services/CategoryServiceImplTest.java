package com.eshop.eshopapi.services;

import com.eshop.eshopapi.dao.Category;
import com.eshop.eshopapi.models.CategoryDto;
import com.eshop.eshopapi.models.CategoryTreeMap;
import com.eshop.eshopapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setup(){
        categoryService = new CategoryServiceImpl(categoryRepository, mongoTemplate);
    }

    @Test
    @DisplayName("Save new category")
    void testSaveCategory() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto("6577", "Grocery", null, true, false, "");

        // Act
        categoryService.saveCategory(categoryDto);

        // Assert
        Mockito.verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Save existing category")
    void testSaveExistingCategory() {
        // Arrange
        Category category = new Category();
        category.setCategoryId("001");
        category.setCategoryName("Apparel");
        category.setParentCategory(null);
        category.setIsChild(false);
        category.setIsParent(true);

        Mockito.when(mongoTemplate.find(any(), any())).thenReturn(List.of(category));
        CategoryDto categoryDto = new CategoryDto("001", "Aparrel", null, true, false, "");

        // Act
        categoryService.saveCategory(categoryDto);

        // Assert
        Mockito.verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Get category by ID")
    void testGetCategoriesById() {
        //Arrange
        String categoryId = "001";
        Category category = new Category();
        category.setCategoryId("001");
        category.setCategoryName("Apparel");
        category.setParentCategory(null);
        category.setIsChild(false);
        category.setIsParent(true);

        Mockito.when(mongoTemplate.find(any(), any())).thenReturn(List.of(category));

        //Act
        categoryService.getCategories(categoryId);

        //Assert
//        Mockito.verify()
    }

    @Test
    @DisplayName("Get category by ID")
    void testGetCategoriesAll() {
        //Arrange
        String categoryId = "all";
        Category category = new Category();
        category.setCategoryId("001");
        category.setCategoryName("Apparel");
        category.setParentCategory(null);
        category.setIsChild(false);
        category.setIsParent(true);

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category));

        //Act
        categoryService.getCategories(categoryId);

        //Assert
        Mockito.verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName("Test getAllCategories")
    void testGetAllCategories() {
        //Arrange
        Category category = new Category();
        category.setCategoryId("001");
        category.setCategoryName("Apparel");
        category.setParentCategory(null);
        category.setIsChild(false);
        category.setIsParent(true);

        Category childCategory = new Category();
        childCategory.setCategoryId("0011");
        childCategory.setCategoryName("T-shirt");
        childCategory.setParentCategory("001");
        childCategory.setIsChild(true);
        childCategory.setIsParent(false);


        CategoryTreeMap categoryMap = new CategoryTreeMap();
        categoryMap.setId("0011");
        categoryMap.setName("T-Shirt");
        categoryMap.setChildren(new ArrayList<>());

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category, childCategory));

        // Act
        categoryService.getAllCategories();

        //Assert
//        Mockito.verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName("Test delete category")
    void testDeleteCategory() {
        // Arrange
        String categoryId = "0011";

        Category childCategory = new Category();
        childCategory.setCategoryId("0011");
        childCategory.setCategoryName("T-shirt");
        childCategory.setParentCategory("001");
        childCategory.setIsChild(true);
        childCategory.setIsParent(false);

        Mockito.when(categoryRepository.findByCategoryId(any())).thenReturn(Optional.of(childCategory));

        // Act
        categoryService.deleteCategory(categoryId);

        // Assert
        Mockito.verify(categoryRepository).delete(childCategory);
    }

    @Test
    @DisplayName("Test delete category")
    void testDeleteCategoryNullCondition() {
        // Arrange
        String categoryId = "00098";

        Mockito.when(categoryRepository.findByCategoryId(any())).thenReturn(Optional.empty());

        // Act
        categoryService.deleteCategory(categoryId);

    }
}