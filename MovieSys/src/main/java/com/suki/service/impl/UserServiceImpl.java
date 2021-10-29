package com.suki.service.impl;

import com.suki.dao.UserMapper;
import com.suki.pojo.User;
import com.suki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByNameOrEmail(String username) {
        return userMapper.getUserByNameOrEmail(username);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public User getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }
}
