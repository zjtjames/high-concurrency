/**
 * created by Zheng Jiateng on 2019/8/13.
 */

package com.jmall.high.redis;

/**
 * 用来缓存MiaoshaOrder到redis中 不设过期时间 永不过期
 */
public class OrderKey extends BasePrefix {

	public OrderKey(String prefix) {
		super(prefix);
	}
	public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
