package com.suki.dao;

import com.suki.pojo.ArticleCategoryRef;
import com.suki.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleCategoryRefMapper {
    List<Category> listCategoryByArticleId(@Param("id") Integer articleId);

    List<Integer> selectCategoryIdByArticleId(@Param("id") Integer articleId);

    void insert(ArticleCategoryRef articleCategoryRef);

    Integer countArticleByCategoryId(@Param("id") Integer categoryId);

    void deleteByArticleId(@Param("id") Integer articleId);
}
