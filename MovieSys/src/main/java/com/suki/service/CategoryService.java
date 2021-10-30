package com.suki.service;

import com.suki.pojo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listCategory();

    List<Category> listCategoryWithCount();
}
