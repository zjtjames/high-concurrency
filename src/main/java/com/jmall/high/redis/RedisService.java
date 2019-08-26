/**
 * created by Zheng Jiateng on 2019/8/1.
 */
package com.jmall.high.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired // 按类型自动注入
    private JedisPool jedisPool; // jedis线程池

    /**
     * 从redis中获取单个对象，先取出json字符串，再用反序列化
     */

    public <T> T get(KeyPrefix prefix, String key,  Class<T> cls) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            String  str = jedis.get(realKey);
            T t =  stringToBean(str, cls);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 将对象序列化成字符串，存入redis
     */
    public <T> boolean set(KeyPrefix prefix, String key,  T value) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            int seconds =  prefix.expireSeconds();
            if(seconds == 0) { // 0代表永不过期
                jedis.set(realKey, str);
            }else {
                // value带过期时间
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除key
     * @param prefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            long ret =  jedis.del(realKey);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * key的value加1 原子操作
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * key的value减1 原子操作
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public static  <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> cls = value.getClass();
        if(cls == int.class || cls == Integer.class) {
            return ""+value;
        }else if(cls == String.class) {
            return (String)value;
        }else if(cls == long.class || cls == Long.class) {
            return ""+value;
        }else {
            // 用fastjson序列化对象
            return JSON.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static  <T> T stringToBean(String str, Class<T> cls) {
        if(str == null || str.length() <= 0 || cls == null) {
            return null;
        }
        if(cls == int.class || cls == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(cls == String.class) {
            return (T)str;
        }else if(cls == long.class || cls == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            // 用fastjson反序列化对象 只支持bean类型
            return JSON.toJavaObject(JSON.parseObject(str), cls);
        }
    }

    // 将用完的连接还给连接池
    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }
}
