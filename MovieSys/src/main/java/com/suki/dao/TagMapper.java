package com.suki.dao;

import com.suki.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper {
    List<Tag> listTag();

    void insert(Tag tag);

    Tag getTagById(@Param("id") Integer id);

    void deleteTag(@Param("id") Integer id);

    void update(Tag tag);

    int countTag();
}
