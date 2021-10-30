package com.suki.dao;

import com.suki.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CommentMapper {
    List<Comment> listRecentComment(@Param("userid") Integer userId, @Param("limit") Integer limit);

    List<Comment> listCommentByArticleId(@Param("id") Integer articleId);

    List<Comment> listComment(HashMap<String, Object> criteria);

    List<Comment> getReceiveComment(List<Integer> articleIds);

    Comment getCommentById(@Param("commentId") Integer id);

    void delCommentById(@Param("id") Integer id);

    void insert(Comment comment);
}
