/**
 * created by Zheng Jiateng on 2019/8/2.
 */

package com.jmall.high.controller;

import com.jmall.high.redis.RedisService;
import com.jmall.high.result.Result;
import com.jmall.high.service.UserService;
import com.jmall.high.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
    UserService userService;
	
	@Autowired
	RedisService redisService;
	
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login"; // spring.thymeleaf.prefix=classpath:/templates/
    }
    
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) { // @Valid是 JSR303参数检验
    	logger.info(loginVo.toString());
    	//登录
    	userService.login(response, loginVo);
    	return Result.success(true);
    }
}
