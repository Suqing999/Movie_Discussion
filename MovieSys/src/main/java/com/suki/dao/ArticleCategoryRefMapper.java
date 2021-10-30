package com.suki.dao;

import com.suki.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleCategoryRefMapper {
    List<Category> listCategoryByArticleId(@Param("id") Integer articleId);
}
