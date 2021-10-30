package com.suki.service.impl;

import com.suki.dao.ArticleTagRefMapper;
import com.suki.dao.TagMapper;
import com.suki.pojo.Tag;
import com.suki.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRefMapper articleTagRefMapper;



    @Override
    public List<Tag> listTag() {
        return tagMapper.listTag();
    }

    @Override
    public List<Tag> listTagWithCount() {
        List<Tag> tagList = null;
        try {
            tagList = tagMapper.listTag();
            for (int i = 0; i < tagList.size(); i++) {
                Integer count = articleTagRefMapper.countArticleByTagId(tagList.get(i).getTagId());
                tagList.get(i).setArticleCount(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tagList;
    }

    @Override
    public void insertTag(Tag tag) {
        try {
            tagMapper.insert(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Tag getTagById(Integer id) {
        Tag tag = null;
        try {
            tag = tagMapper.getTagById(id);
        } catch (Exception e) {            e.printStackTrace();
        }
        return tag;
    }

    @Override
    public void deleteTag(Integer id) {
        tagMapper.deleteTag(id);
    }

    @Override
    public void updateTag(Tag tag) {
        tagMapper.update(tag);
    }
}
