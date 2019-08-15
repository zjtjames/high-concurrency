/**
 * created by Zheng Jiateng on 2019/8/2.
 */

package com.jmall.high.controller;

import com.jmall.high.pojo.MiaoshaUser;
import com.jmall.high.redis.RedisService;
import com.jmall.high.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;

    @RequestMapping("/to_list")
    public String list(Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        return "goods_list"; // spring.thymeleaf.prefix=classpath:/templates/
    }
}
