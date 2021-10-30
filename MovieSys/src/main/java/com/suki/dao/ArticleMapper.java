package com.suki.dao;

import com.suki.pojo.Article;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface ArticleMapper {

    List<Article> listRecentArticle(@Param("userid") Integer userId,@Param("limit") Integer limit);

    Article getArticleByStatusAndId(@Param("stat")Integer stat, @Param("id")Integer commentArticleId);

    List<Article> findAll(HashMap<String, Object> criteria);
}
