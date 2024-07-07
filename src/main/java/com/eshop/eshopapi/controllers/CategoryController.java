package com.eshop.eshopapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eshop.eshopapi.dao.Category;
import com.eshop.eshopapi.models.CategoryDto;
import com.eshop.eshopapi.models.CategoryTreeMap;
import com.eshop.eshopapi.services.CategoryService;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories/{searchString}")
    public List<CategoryDto> getArticles(@PathVariable("searchString") String categoryId) {
        return categoryService.getCategories(categoryId);
    }

    @PostMapping("/saveCategory")
    public ResponseEntity saveCategory(@RequestBody CategoryDto categoryDto) {
        Category savedCategory = categoryService.saveCategory(categoryDto);
        if(savedCategory.getId()!=null)
            return ResponseEntity.status(HttpStatus.OK).body("Category saved successfully.");
        else
            return ResponseEntity.internalServerError().body("Error while saving category.");
    }

    @GetMapping("/categoryTree")
    public ResponseEntity getCategoryTree() {
        List<CategoryTreeMap> allCategories = categoryService.getAllCategories();

        if(!allCategories.isEmpty()) 
            return ResponseEntity.status(HttpStatus.OK).body(allCategories);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No categories found.");
    }

    @PostMapping("/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") String categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
    

}
