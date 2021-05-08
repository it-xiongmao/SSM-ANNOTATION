package com.bruce.service.impl;

import com.bruce.mapper.UserMapper;
import com.bruce.pojo.User;
import com.bruce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: Spring-MyBatis-2021
 * @BelongsPackage: com.sm.service.impl
 * @CreateTime: 2021-05-08 15:26
 * @Description: TODO
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    public List<User> findUsers() {
        return userMapper.findUsers();
    }

    public int addUser(User user) {
        return userMapper.addUser(user);
    }
}
