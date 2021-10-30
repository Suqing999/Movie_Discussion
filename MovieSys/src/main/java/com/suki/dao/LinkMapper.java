package com.suki.dao;

import com.suki.pojo.Link;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LinkMapper {
    List<Link> listLink(Integer stat);

    int countLink(@Param("stat") Integer i);
}
