/**
 * created by Zheng Jiateng on 2019/8/1.
 */

package com.jmall.high.redis;

// 区分模块 用来保证不同的模块生成的redis的key不重
public interface KeyPrefix {
		
    int expireSeconds();
	
    String getPrefix();
	
}
