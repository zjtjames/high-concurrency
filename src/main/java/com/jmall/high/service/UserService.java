/**
 * created by Zheng Jiateng on 2019/8/2.
 */

package com.jmall.high.service;

import com.jmall.high.dao.UserMapper;
import com.jmall.high.pojo.MiaoshaUser;
import com.jmall.high.exception.GlobalException;
import com.jmall.high.redis.UserKey;
import com.jmall.high.redis.RedisService;
import com.jmall.high.result.CodeMsg;
import com.jmall.high.util.MD5Util;
import com.jmall.high.util.UUIDUtil;
import com.jmall.high.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {
	
	
	public static final String COOKI_NAME_TOKEN = "token";
	
	@Autowired
    UserMapper userMapper;
	
	@Autowired
	RedisService redisService;
	
	public MiaoshaUser getById(long id) {
		return userMapper.getById(id);
	}
	
    // 根据sessionid从redis中拿User对象
	public MiaoshaUser getByToken(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoshaUser user = redisService.get(UserKey.token, token, MiaoshaUser.class);
		// 刷新有效期：1.重新往redis缓存中写session 2.重新设置cookie
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}
	

	public boolean login(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		// 判断手机号是否存在 用手机号当id
		MiaoshaUser user = getById(Long.parseLong(mobile));
		if(user == null) {
            // 抛出的异常会被通过AOP实现的GlobalExceptionHandler拦截,然后返回Result.error(CodeMsg.SERVER_ERROR)等服务响应对象
		    // 程序在执行完throw语句时立即终止，它后面的语句都不执行，类似于循环中的break。
            // 所以能走到最后的就肯定是没问题的
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//生成cookie
		String token = UUIDUtil.uuid();
		addCookie(response, token, user);
		return true;
	}
	
	private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
		redisService.set(UserKey.token, token, user);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
