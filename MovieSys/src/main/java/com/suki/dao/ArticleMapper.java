package com.suki.dao;

import com.suki.pojo.Article;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface ArticleMapper {

    List<Article> listRecentArticle(@Param("userid") Integer userId,@Param("limit") Integer limit);

    Article getArticleByStatusAndId(@Param("stat")Integer stat, @Param("id")Integer commentArticleId);

    List<Article> findAll(HashMap<String, Object> criteria);

    List<Article> listArticleByViewCount(@Param("limit") Integer limit);

    Article getAfterArticle(@Param("id") Integer articleId);

    Article getPreArticle(@Param("id")Integer articleId);

    List<Article> listRandomArticle(@Param("limit")Integer limit);

    List<Article> listArticleByCommentCount(@Param("limit") Integer limit);

    List<Article> findArticleByCategoryIds(@Param("categoryIds") List<Integer> categoryIds,
                                           @Param("limit") Integer limit);

    void insert(Article article);

    List<Integer> listArticleIdsByUserId(@Param("userId") Integer userId);

    void updateArticle(Article article);

    void delart(@Param("id") Integer id);

    Integer countArticle(@Param("id") Integer status);

    int countArticleComment();

    int countArticleView();

    Article getLastUpdateArticle();

    List<Article> listAllNotWithContent();
}
