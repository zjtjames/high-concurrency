/**
 * created by Zheng Jiateng on 2019/8/2.
 */

package com.jmall.high.redis;

public class UserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

	public static UserKey token = new UserKey(TOKEN_EXPIRE, "tk");
}
