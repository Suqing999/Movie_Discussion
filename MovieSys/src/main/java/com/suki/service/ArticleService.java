package com.suki.service;

import com.github.pagehelper.PageInfo;
import com.suki.pojo.Article;

import java.util.HashMap;
import java.util.List;

public interface ArticleService {
    List<Article> listRecentArticle(Integer userId, Integer limit);

    PageInfo<Article> pageArticle(Integer pageIndex, Integer pageSize, HashMap<String, Object> criteria);

    Article getArticleByStatusAndId(Integer status, Integer articleId);

    List<Article> listArticleByViewCount(Integer limit);

    Article getAfterArticle(Integer articleId);

    Article getPreArticle(Integer articleId);

    List<Article> listRandomArticle(Integer limit);

    List<Article> listArticleByCommentCount(Integer limit);

    List<Article> listArticleByCategoryIds(List<Integer> categoryIds, Integer limit);

    List<Integer> listCategoryIdByArticleId(Integer articleId);

    void insertArticle(Article article);

    void updateArticle(Article article);

    void delArticle(Integer id);

    void updateArticleDetail(Article article);

    int countArticleByCategoryId(Integer id);

    Integer countArticleByTagId(Integer id);

    int countArticle(Integer status);

    int countArticleComment();

    int countArticleView();

    Article getLastUpdateArticle();

    List<Article> listAllNotWithContent();
}
