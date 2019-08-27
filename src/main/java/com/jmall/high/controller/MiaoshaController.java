/**
 * created by Zheng Jiateng on 2019/8/9.
 */

package com.jmall.high.controller;

import com.jmall.high.pojo.MiaoshaOrder;
import com.jmall.high.pojo.MiaoshaUser;
import com.jmall.high.pojo.OrderInfo;
import com.jmall.high.rabbitmq.MQSender;
import com.jmall.high.rabbitmq.MiaoshaMessage;
import com.jmall.high.redis.GoodsKey;
import com.jmall.high.redis.RedisService;
import com.jmall.high.result.CodeMsg;
import com.jmall.high.result.Result;
import com.jmall.high.service.GoodsService;
import com.jmall.high.service.MiaoshaService;
import com.jmall.high.service.UserService;
import com.jmall.high.service.OrderService;
import com.jmall.high.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{

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

    @Autowired
    MQSender sender;


    @Override
    /**
     * 系统初始化
     * 框架启动时，发现有类实现了InitializingBean，就会回调afterPropertiesSet方法
     */
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goods : goodsVoList) {
            // 在系统初始化时，将商品库存加载到redis缓存中
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
        }
    }

    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
	@ResponseBody
    public Result<Integer> doMiaosha(Model model, MiaoshaUser user,
    		@RequestParam("goodsId")long goodsId) { //@RequestParam("goodsId") 接收前端post提交到表单里的参数
    	model.addAttribute("user", user);
    	if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
    	}

        // redis中减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if(stock < 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 判断是否已经秒杀过一次了，查询订单就说明秒杀到一次了，不能再重复秒杀，返回秒杀失败页面
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        // 入队 sender 消息的生产者
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        return Result.success(0);//排队中

    	/*
    	// 判断商品是否还有库存，没有库存就返回秒杀失败页面
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	int stock = goods.getStockCount();
    	if(stock <= 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
    	}
    	// 判断是否已经秒杀过一次了，查询订单就说明秒杀到一次了，不能再重复秒杀，返回秒杀失败页面
    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
    	if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
    	}
    	// 减库存 下订单 写入秒杀订单 用事务保证原子性 事务中的操作全成功或全回滚
    	OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
    	// 秒杀成功 返回订单详情页
        return Result.success(orderInfo);
        */
    }

    /**
     *
     * @return
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,MiaoshaUser user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }
}
