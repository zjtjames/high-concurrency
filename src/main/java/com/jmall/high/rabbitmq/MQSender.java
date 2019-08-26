/**
 * created by Zheng Jiateng on 2019/8/15.
 */

package com.jmall.high.rabbitmq;

import com.jmall.high.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    private static Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate; // 引入amqp依赖后系统自动帮我们注入的操作queue的工具类

    public void sendMiaoshaMessage(MiaoshaMessage mm) {
        String msg = RedisService.beanToString(mm);
        logger.info("send message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg); // 发送到哪个queue
    }

    // 简单队列
	public void send(Object message) {
		String msg = RedisService.beanToString(message);
        logger.info("send message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
	}

	// 主题
	public void sendTopic(Object message) {
		String msg = RedisService.beanToString(message);
        logger.info("send topic message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
	}

	// 广播
	public void sendFanout(Object message) {
		String msg = RedisService.beanToString(message);
        logger.info("send fanout message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
	}

	public void sendHeader(Object message) {
		String msg = RedisService.beanToString(message);
        logger.info("send fanout message:" + msg);
        MessageProperties properties = new MessageProperties();
		properties.setHeader("header1", "value1");
		properties.setHeader("header2", "value2");
		Message obj = new Message(msg.getBytes(), properties);
		amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
	}
}
