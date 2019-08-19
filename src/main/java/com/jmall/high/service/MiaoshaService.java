/**
 * created by Zheng Jiateng on 2019/8/9.
 */

package com.jmall.high.service;

import com.jmall.high.pojo.MiaoshaUser;
import com.jmall.high.pojo.OrderInfo;
import com.jmall.high.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;

	@Transactional
    // 用事务保证原子性 事务中的操作全成功或全回滚
	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		goodsService.reduceStock(goods);
		//两步操作 先在order_info表中插入订单 再在maiosha_order中插入订单
		return orderService.createOrder(user, goods);
	}
	
}
