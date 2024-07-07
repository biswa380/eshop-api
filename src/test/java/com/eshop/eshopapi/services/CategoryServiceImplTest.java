package com.eshop.eshopapi.services;

import com.eshop.eshopapi.dao.Category;
import com.eshop.eshopapi.models.CategoryDto;
import com.eshop.eshopapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

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
}