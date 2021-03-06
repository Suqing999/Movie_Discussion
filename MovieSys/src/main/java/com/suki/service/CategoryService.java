package com.suki.service;

import com.suki.pojo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listCategory();

    List<Category> listCategoryWithCount();

    void insertCategory(Category category);

    Category getCategoryById(Integer id);

    void deleteCategory(Integer id);

    void updateCategory(Category category);

    int countCategory();
}
