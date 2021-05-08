package com.bruce.mapper;

import com.bruce.pojo.User;

import java.util.List;

/**
 * @BelongsProject: Spring-MyBatis-2021
 * @BelongsPackage: com.sm.mapper
 * @CreateTime: 2021-05-08 15:21
 * @Description: TODO
 */
public interface UserMapper {

    List<User> findUsers();

    int addUser(User user);
}
