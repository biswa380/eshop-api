package com.eshop.eshopapi.models;

import org.springframework.data.mongodb.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {

    public String categoryId;

    public String categoryName;

    public String parentId;

    public Boolean isParent;

    public Boolean isChild;

    public String displayImage;
}
