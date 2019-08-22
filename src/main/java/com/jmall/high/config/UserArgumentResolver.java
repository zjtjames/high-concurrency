/**
 * created by Zheng Jiateng on 2019/8/3.
 */

package com.jmall.high.config;

import com.jmall.high.pojo.MiaoshaUser;
import com.jmall.high.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
// 解析出User对象 作为参数传给需要的Controller
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	UserService userService;
	
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> cls = parameter.getParameterType();
        return cls == MiaoshaUser.class;
    }

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		
		String paramToken = request.getParameter(UserService.COOKI_NAME_TOKEN);
		String cookieToken = getCookieValue(request, UserService.COOKI_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		// 根据cookie中的sessionid从redis中拿到User对象
		return userService.getByToken(response, token);
	}

	private String getCookieValue(HttpServletRequest request, String cookiName) {
		Cookie[]  cookies = request.getCookies();
        if(cookies == null || cookies.length < 1){
            return null;
        }
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(cookiName)) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
