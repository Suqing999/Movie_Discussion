package com.suki.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suki.dao.ArticleMapper;
import com.suki.dao.CommentMapper;
import com.suki.pojo.Article;
import com.suki.pojo.Comment;
import com.suki.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public List<Comment> listCommentByArticleId(Integer articleId) {

        return commentMapper.listCommentByArticleId(articleId);
    }

    @Override
    public PageInfo<Comment> listCommentByPage(Integer pageIndex, Integer pageSize, HashMap<String, Object> criteria) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Comment> commentList = null;
        try {
            commentList = commentMapper.listComment(criteria);
            for (int i = 0; i < commentList.size(); i++) {
                Article article = articleMapper.getArticleByStatusAndId(1, commentList.get(i).getCommentArticleId());
                commentList.get(i).setArticle(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.error("分页获得评论失败,pageIndex:{}, pageSize:{}, cause:{}", pageIndex, pageSize, e);
        }
        return new PageInfo<>(commentList);
    }

    @Override
    public PageInfo<Comment> listReceiveCommentByPage(Integer pageIndex, Integer pageSize, Integer userId) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Comment> commentList = new ArrayList<>();
        try {
            List<Integer> articleIds = articleMapper.listArticleIdsByUserId(userId);
            if (articleIds != null && articleIds.size() > 0) {
                commentList = commentMapper.getReceiveComment(articleIds);
                for (int i = 0; i < commentList.size(); i++) {
                    Article article = articleMapper.getArticleByStatusAndId(1, commentList.get(i).getCommentArticleId());
                    commentList.get(i).setArticle(article);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
           // log.error("分页获得评论失败,pageIndex:{}, pageSize:{}, cause:{}", pageIndex, pageSize, e);
        }
        return new PageInfo<>(commentList);
    }

    @Override
    public Comment getCommentById(Integer id) {
        Comment comment = commentMapper.getCommentById(id);

        return comment;
    }

    @Override
    public void deleteCommentById(Integer id) {
        commentMapper.delCommentById(id);
    }

    @Override
    public void insertComment(Comment comment) {
        commentMapper.insert(comment);
    }


}
