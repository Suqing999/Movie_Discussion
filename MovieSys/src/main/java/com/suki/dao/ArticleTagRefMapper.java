package com.suki.dao;

import com.suki.pojo.ArticleTagRef;
import com.suki.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleTagRefMapper {


    List<Tag> listTagByArticleId(@Param("id") Integer articleId);

    void insert(ArticleTagRef articleTagRef);

    Integer countArticleByTagId(@Param("id") Integer tagId);

    void deleteByArticleId(@Param("id") Integer articleId);
}
