package com.suki.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private Integer articleId;
    private Integer articleUserId;
    private String articleTitle;
    private Integer articleViewCount;
    private Integer articleCommentCount;
    private Integer articleLikeCount;
    private Date articleCreateTime;
    private Date articleUpdateTime;
    private Integer articleIsComment;
    private Integer articleStatus;
    private Integer articleOrder;
    private String articleContent;
    private String articleSummary;
    private String articleThumbnail;
    private User user;

    private List<Tag> tagList;
    private List<Category> categoryList;
}
