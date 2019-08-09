/**
 * created by Zheng Jiateng on 2019/8/2.
 */

package com.jmall.high.util;

import java.util.UUID;

public class UUIDUtil {
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
