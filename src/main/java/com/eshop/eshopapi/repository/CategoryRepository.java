package com.eshop.eshopapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.eshop.eshopapi.dao.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String>{
    public Category save(Category category);

    public Optional<Category> findByCategoryId(String id);

    public List<Category> findAll();

}
