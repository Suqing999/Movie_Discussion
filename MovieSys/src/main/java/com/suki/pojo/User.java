package com.suki.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;
    private String userName;
    private String userPass;
    private String userNickname;
    private String userEmail;
    private String userUrl;
    private String userAvatar;
    private String userLastLoginIp;
    private Date userRegisterTime;
    private Date userLastLoginTime;
    private Integer userStatus;
    /**
     * 用户角色：admin/user
     */
    private String userRole;
    /**
     * 文章数量（不是数据库字段）
     */
    private Integer articleCount;
}
