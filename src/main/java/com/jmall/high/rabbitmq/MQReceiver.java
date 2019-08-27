/**
 * created by Zheng Jiateng on 2019/8/15.
 */

package com.jmall.high.rabbitmq;

import com.jmall.high.pojo.MiaoshaOrder;
import com.jmall.high.pojo.MiaoshaUser;
import com.jmall.high.redis.RedisService;
import com.jmall.high.service.GoodsService;
import com.jmall.high.service.MiaoshaService;
import com.jmall.high.service.OrderService;
import com.jmall.high.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE) // 从哪个queue中读数据
    public void receive(String message) {
        logger.info("receive message:" + message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        // 判断是否已经秒杀过一次了，查询订单就说明秒杀到一次了，不能再重复秒杀，直接返回 什么也不做
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        // 减库存 下订单 写入秒杀订单 用事务保证原子性 事务中的操作全成功或全回滚
        miaoshaService.miaosha(user, goods);
    }

//		@RabbitListener(queues=MQConfig.QUEUE)
//		public void receive(String message) {
//			logger.info("receive message:"+message);
//		}
//
//		@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
//		public void receiveTopic1(String message) {
//			logger.info(" topic  queue1 message:"+message);
//		}
//
//		@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
//		public void receiveTopic2(String message) {
//			logger.info(" topic  queue2 message:"+message);
//		}
//
//		@RabbitListener(queues=MQConfig.HEADER_QUEUE)
//		public void receiveHeaderQueue(byte[] message) {
//			logger.info(" header  queue message:"+new String(message));
//		}
//
}
