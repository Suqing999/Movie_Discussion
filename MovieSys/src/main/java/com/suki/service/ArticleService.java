package com.suki.service;

import com.suki.pojo.Article;

import java.util.List;

public interface ArticleService {
    List<Article> listRecentArticle(Integer userId, Integer limit);
}
