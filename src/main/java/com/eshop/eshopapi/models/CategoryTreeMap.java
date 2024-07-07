package com.eshop.eshopapi.models;

import java.util.List;

import lombok.Data;

@Data
public class CategoryTreeMap {
    public String id;
    public String name;
    public List<CategoryTreeMap> children;
}
