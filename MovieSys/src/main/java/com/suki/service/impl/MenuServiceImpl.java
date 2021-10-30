package com.suki.service.impl;

import com.suki.dao.MenuMapper;
import com.suki.pojo.Menu;
import com.suki.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> listMenu() {
        return menuMapper.listMenu();
    }
}
