/**
 * created by Zheng Jiateng on 2019/8/1.
 */


package com.jmall.high.redis;

public class UserKey extends BasePrefix{

	private UserKey(String prefix) {
		super(prefix);
	}
	// 静态变量
	public static UserKey getById = new UserKey("id");

	public static UserKey getByName = new UserKey("name");
}
