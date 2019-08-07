/**
 * created by Zheng Jiateng on 2019/8/7.
 */
package com.jmall.high.service;

import com.jmall.high.dao.UserMapper;
import com.jmall.high.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getById(int id) {
        return userMapper.getById(id);
    }
}
