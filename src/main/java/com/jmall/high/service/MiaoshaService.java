/**
 * created by Zheng Jiateng on 2019/8/9.
 */

package com.jmall.high.service;

import com.jmall.high.pojo.MiaoshaOrder;
import com.jmall.high.pojo.MiaoshaUser;
import com.jmall.high.pojo.OrderInfo;
import com.jmall.high.redis.MiaoshaKey;
import com.jmall.high.redis.RedisService;
import com.jmall.high.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MiaoshaService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;

    @Autowired
    RedisService redisService;


	@Transactional
    // 用事务保证原子性 事务中的操作全成功或全回滚
	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        // 如果减库存成功
        if (success) {
            //两步操作 先在order_info表中插入订单 再在maiosha_order中插入订单
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId()); // 减库存不成功
            return null;
        }
	}

    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if(order != null) {// 订单已经生成，秒杀成功
            return order.getOrderId();
        }else {
            // 是否已经卖完
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1; // 已经卖完 失败
            }else {
                return 0; // 没有卖完 继续轮询
            }
        }
    }

    // 已经卖完 在redis中设置该商品卖完的标记
    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    // 从redis中查看该商品是否已经卖完
    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }


}
