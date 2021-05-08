package com.bruce.service;


import com.bruce.pojo.User;

import java.util.List;

/**
 * @BelongsProject: Spring-MyBatis-2021
 * @BelongsPackage: com.sm.service
 * @CreateTime: 2021-05-08 15:25
 * @Description: TODO
 */
public interface UserService {

    List<User> findUsers();

    int addUser(User user);

}
