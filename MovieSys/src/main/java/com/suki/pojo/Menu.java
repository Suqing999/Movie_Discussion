package com.suki.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Menu implements Serializable {
    private Integer menuId;

    private String menuName;

    private String menuUrl;

    private Integer menuLevel;

    private String menuIcon;

    private Integer menuOrder;

}