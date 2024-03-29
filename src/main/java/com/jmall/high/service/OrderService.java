/**
 * created by Zheng Jiateng on 2019/8/9.
 */

package com.jmall.high.service;

import com.jmall.high.dao.OrderMapper;
import com.jmall.high.pojo.MiaoshaOrder;
import com.jmall.high.pojo.MiaoshaUser;
import com.jmall.high.pojo.OrderInfo;
import com.jmall.high.redis.OrderKey;
import com.jmall.high.redis.RedisService;
import com.jmall.high.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
	
	@Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisService redisService;

	// 从redis缓存中根据用户id和商品id查询是否有秒杀订单
	public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
//		return orderMapper.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, ""+userId+"_"+goodsId, MiaoshaOrder.class);
	}

	public OrderInfo getOrderById(long orderId) {
		return orderMapper.getOrderById(orderId);
	}

	@Transactional
	public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getMiaoshaPrice()); // 价格为秒杀时的价格
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		orderMapper.insert(orderInfo); // 先在order_info表中插入订单
		MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
		miaoshaOrder.setGoodsId(goods.getId());
		miaoshaOrder.setOrderId(orderInfo.getId());
		miaoshaOrder.setUserId(user.getId());
        orderMapper.insertMiaoshaOrder(miaoshaOrder); // 再在maiosha_order中插入秒杀订单
        //缓存秒杀订单MiaoshaOrder到redis中
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, ""+user.getId()+"_"+goods.getId(), miaoshaOrder);
		return orderInfo;
	}
}
