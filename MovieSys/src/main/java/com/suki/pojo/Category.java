package com.suki.pojo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author liuyanzhao
 */
@Data
public class Category implements Serializable {
    private Integer categoryId;
    private Integer categoryPid;
    private String categoryName;
    private String categoryDescription;
    private Integer categoryOrder;
    private String categoryIcon;
    /**
     * 文章数量(非数据库字段)
     */
    private Integer articleCount;
    public Category(Integer categoryId, Integer categoryPid, String categoryName, String categoryDescription, Integer categoryOrder, String categoryIcon, Integer articleCount) {
        this.categoryId = categoryId;
        this.categoryPid = categoryPid;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryOrder = categoryOrder;
        this.categoryIcon = categoryIcon;
        this.articleCount = articleCount;
    }
    public Category(Integer categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
    public Category(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public Category() {}


}
