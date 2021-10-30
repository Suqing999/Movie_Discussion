package com.suki.service;

import com.suki.pojo.Tag;

import java.util.List;

public interface TagService {
    List<Tag> listTag();

    List<Tag> listTagWithCount();

    void insertTag(Tag tag);

    Tag getTagById(Integer id);

    void deleteTag(Integer id);

    void updateTag(Tag tag);
}
