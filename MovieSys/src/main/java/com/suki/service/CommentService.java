package com.suki.service;

import com.github.pagehelper.PageInfo;
import com.suki.pojo.Comment;

import java.util.HashMap;
import java.util.List;

public interface CommentService {

    List<Comment> listRecentComment(Integer userId, Integer limit);

    List<Comment> listCommentByArticleId(Integer articleId);

    PageInfo<Comment> listCommentByPage(Integer pageIndex, Integer pageSize, HashMap<String, Object> criteria);

    PageInfo<Comment> listReceiveCommentByPage(Integer pageIndex, Integer pageSize, Integer userId);

    Comment getCommentById(Integer id);

    void deleteCommentById(Integer id);

    void insertComment(Comment comment);
}
