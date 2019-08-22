/**
 * created by Zheng Jiateng on 2019/8/12.
 */

package com.jmall.high.redis;

public class GoodsKey extends BasePrefix{

	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	// 对于页面缓存 有效期一般设置较短的时间
	public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
	public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
}
