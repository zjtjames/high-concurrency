/**
 * created by Zheng Jiateng on 2019/8/9.
 */

package com.jmall.high.controller;

import com.jmall.high.pojo.MiaoshaUser;
import com.jmall.high.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    /**
     * 从redis中获取用户对象 为了做jmeter压测设计的接口
     * 请求中要带token信息 可以些在url参数中，也可以写在cookie中 jmeter都支持
     */
    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model,MiaoshaUser user) {
        return Result.success(user);
    }
    
}
