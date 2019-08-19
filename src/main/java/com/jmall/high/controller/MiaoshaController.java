/**
 * created by Zheng Jiateng on 2019/8/9.
 */

package com.jmall.high.controller;

import com.jmall.high.pojo.MiaoshaOrder;
import com.jmall.high.pojo.MiaoshaUser;
import com.jmall.high.pojo.OrderInfo;
import com.jmall.high.redis.RedisService;
import com.jmall.high.result.CodeMsg;
import com.jmall.high.service.GoodsService;
import com.jmall.high.service.MiaoshaService;
import com.jmall.high.service.UserService;
import com.jmall.high.service.OrderService;
import com.jmall.high.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoshaService miaoshaService;
	
    @RequestMapping("/do_miaosha")
    public String list(Model model, MiaoshaUser user,
    		@RequestParam("goodsId")long goodsId) { //@RequestParam("goodsId") 接收前端post提交到表单里的参数
    	model.addAttribute("user", user);
    	if(user == null) {
    		return "login";
    	}
    	// 判断商品是否还有库存，没有库存就返回秒杀失败页面
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	int stock = goods.getStockCount();
    	if(stock <= 0) {
    		model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
    		return "miaosha_fail";
    	}
    	// 判断是否已经秒杀到了，查询订单就说明秒杀到了，不能再重复秒杀，返回秒杀失败页面
    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
    	if(order != null) {
    		model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
    		return "miaosha_fail";
    	}
    	//减库存 下订单 写入秒杀订单 用事务保证原子性 事务中的操作全成功或全回滚
    	OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
    	model.addAttribute("orderInfo", orderInfo);
    	model.addAttribute("goods", goods);
    	// 返回下单成功的页面
        return "order_detail";
    }
}
