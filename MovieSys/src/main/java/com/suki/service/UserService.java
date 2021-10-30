package com.suki.service;

import com.suki.pojo.User;

import java.util.List;

public interface UserService {

    public User getUserByNameOrEmail(String username);

    void updateUser(User user);

    User getUserByName(String userName);

    User getUserByEmail(String email);

    void insertUser(User user);

    User getUserById(Integer userId);

    List<User> listUser();

    void deleteUser(Integer id);
}
