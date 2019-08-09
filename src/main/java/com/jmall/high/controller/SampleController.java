/**
 * created by Zheng Jiateng on 2019/8/1.
 */
package com.jmall.high.controller;

import com.jmall.high.pojo.User;
import com.jmall.high.redis.RedisService;
import com.jmall.high.redis.UserKey;
import com.jmall.high.result.Result;
import com.jmall.high.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "james");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody // 将返回值序列化为json
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody // 将返回值序列化为json
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById, "" + 1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody // 将返回值序列化为json
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(2);
        user.setName("james2");
        redisService.set(UserKey.getById, "" + 2, user); // UserKey:id2
        return Result.success(true);
    }
}
