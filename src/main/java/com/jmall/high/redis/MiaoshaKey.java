/**
 * created by Zheng Jiateng on 2019/8/16.
 */

package com.jmall.high.redis;

public class MiaoshaKey extends BasePrefix{

	private MiaoshaKey(String prefix) {
		super(prefix);
	}

	public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");
}
