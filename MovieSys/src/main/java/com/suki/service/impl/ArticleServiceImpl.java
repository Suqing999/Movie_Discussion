package com.suki.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suki.dao.ArticleCategoryRefMapper;
import com.suki.dao.ArticleMapper;
import com.suki.dao.UserMapper;
import com.suki.pojo.Article;
import com.suki.pojo.Category;
import com.suki.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleCategoryRefMapper articleCategoryRefMapper;


    @Override
    public List<Article> listRecentArticle(Integer userId, Integer limit) {
        return articleMapper.listRecentArticle(userId, limit);
    }

    @Override
    public PageInfo<Article> pageArticle(Integer pageIndex,
                                         Integer pageSize,
                                         HashMap<String, Object> criteria) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Article> articleList = articleMapper.findAll(criteria);
        for (int i = 0; i < articleList.size(); i++) {
            //封装CategoryList
            List<Category> categoryList = articleCategoryRefMapper.listCategoryByArticleId(articleList.get(i).getArticleId());
            if (categoryList == null || categoryList.size() == 0) {
                categoryList = new ArrayList<>();
                categoryList.add(new Category(100000000, "未分类"));
            }
            articleList.get(i).setCategoryList(categoryList);

            articleList.get(i).setUser(userMapper.getUserById(articleList.get(i).getArticleUserId()));

        }
        return new PageInfo<>(articleList);
    }
}
