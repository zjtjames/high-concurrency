/**
 * created by Zheng Jiateng on 2019/8/2.
 */

package com.jmall.high.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	private static final String salt = "1a2b3c4d";
	
	public static String inputPassToFormPass(String inputPass) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}
	
	public static String formPassToDBPass(String formPass, String salt) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}
	
	public static String inputPassToDbPass(String inputPass, String saltDB) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, saltDB);
		return dbPass;
	}
	
	public static void main(String[] args) {
		System.out.println(inputPassToDbPass("000000", "1a2b3c4d"));//5e7b3a9754c2777f96174d4ccb980d23
	}
	
}
