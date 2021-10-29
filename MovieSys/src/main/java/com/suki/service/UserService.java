package com.suki.service;

import com.suki.pojo.User;

public interface UserService {

    public User getUserByNameOrEmail(String username);

    void updateUser(User user);

    User getUserByName(String userName);

    User getUserByEmail(String email);

    void insertUser(User user);
}
