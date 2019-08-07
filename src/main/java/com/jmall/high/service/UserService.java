/**
 * created by Zheng Jiateng on 2019/8/7.
 */
package com.jmall.high.service;

import com.jmall.high.dao.UserMapper;
import com.jmall.high.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getById(int id) {
        return userMapper.getById(id);
    }

    @Transactional // 测试事务
    public boolean tx() {
        User u1 = new User();
        u1.setId(2);
        u1.setName("222");
        userMapper.insert(u1);

        User u2 = new User();
        u2.setId(1);
        u2.setName("666");
        userMapper.insert(u2);
        return true;
    }
}
