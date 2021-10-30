package com.suki.dao;

import com.suki.pojo.Link;

import java.util.List;

public interface LinkMapper {
    List<Link> listLink(Integer stat);
}
