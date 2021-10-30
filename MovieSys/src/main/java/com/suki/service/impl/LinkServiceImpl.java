package com.suki.service.impl;

import com.suki.dao.LinkMapper;
import com.suki.pojo.Link;
import com.suki.service.LinkService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkMapper linkMapper;

    @Override
    public List<Link> listLink(Integer stat) {
        return linkMapper.listLink(stat);
    }

    @Override
    public int countLink(Integer i) {
        return linkMapper.countLink(i);
    }
}
