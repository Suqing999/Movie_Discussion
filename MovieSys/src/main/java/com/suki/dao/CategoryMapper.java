package com.suki.dao;

import com.suki.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    List<Category> listCategory();

    void insert(Category category);

    Category getCategoryById(@Param("id") Integer id);

    void deleteCategory(@Param("id") Integer id);

    void update(Category category);

    int countCategory();
}
