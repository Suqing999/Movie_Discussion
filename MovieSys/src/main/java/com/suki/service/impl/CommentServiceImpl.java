package com.suki.service.impl;

import com.suki.dao.ArticleMapper;
import com.suki.dao.CommentMapper;
import com.suki.pojo.Article;
import com.suki.pojo.Comment;
import com.suki.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Comment> listRecentComment(Integer userId, Integer limit) {

        List<Comment> commentList = null;
        try {
            commentList = commentMapper.listRecentComment(userId, limit);

            for (int i = 0; i < commentList.size(); i++) {
                Article article = articleMapper.getArticleByStatusAndId(1, commentList.get(i).getCommentArticleId());
                commentList.get(i).setArticle(article); //设置评论到对应的文章
            }

        } catch (Exception e) {
            e.printStackTrace();
            //log.error("获得最新评论失败, limit:{}, cause:{}", limit, e);
        }
        return commentList;

    }
}
