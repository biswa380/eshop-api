package com.eshop.eshopapi.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Category {
    @Id
    public String id;

    public String categoryId;

    @Indexed
    public String categoryName;

    public Boolean isParent;

    public Boolean isChild;

    public String parentCategory;

    public String displayImage;
}
