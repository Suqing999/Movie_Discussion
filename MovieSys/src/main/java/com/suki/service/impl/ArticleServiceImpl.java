package com.suki.service.impl;

import com.suki.dao.ArticleMapper;
import com.suki.pojo.Article;
import com.suki.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public List<Article> listRecentArticle(Integer userId, Integer limit) {
        return articleMapper.listRecentArticle(userId, limit);
    }
}
