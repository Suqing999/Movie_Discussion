package com.suki.service;

import com.github.pagehelper.PageInfo;
import com.suki.pojo.Article;

import java.util.HashMap;
import java.util.List;

public interface ArticleService {
    List<Article> listRecentArticle(Integer userId, Integer limit);

    PageInfo<Article> pageArticle(Integer pageIndex, Integer pageSize, HashMap<String, Object> criteria);
}
