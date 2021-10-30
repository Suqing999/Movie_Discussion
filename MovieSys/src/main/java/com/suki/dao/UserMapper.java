package com.suki.dao;

import com.suki.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    User getUserByNameOrEmail(@Param("username") String username);

    void updateUser(User user);

    User getUserByName(@Param("username") String userName);

    User getUserByEmail(@Param("email") String email);

    void insertUser(User user);

    User getUserById(@Param("id") Integer userId);

    List<User> listUser();

    void deleteUser(@Param("id") Integer id);
}
