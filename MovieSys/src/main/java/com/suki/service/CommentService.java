package com.suki.service;

import com.suki.pojo.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listRecentComment(Integer userId, Integer limit);
}
