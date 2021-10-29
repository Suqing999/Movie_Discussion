package com.suki.dao;

import com.suki.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {
    List<Comment> listRecentComment(@Param("userid") Integer userId, @Param("limit") Integer limit);
}
