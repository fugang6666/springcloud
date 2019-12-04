package com.spring.gateway.util;

public class Base64Utils {
	/**
	 * 对字符串进行base64编码
	 * 
	 * @param s
	 *            需要进行编码处理的字符串
	 * @return 编码后的字符串，如果s为null或者空字符串则直接返回
	 */
	public static String encode(String s) {
		if (StringUtils.isNullOrEmpty(s)) {
			return s;
		}
		return org.springframework.util.Base64Utils.encodeToString(s.getBytes());
	}

	/**
	 * 对字节数组进行base64编码
	 * 
	 * @param b
	 *            需要进行编码处理的字符串
	 * @return 编码后的字符串，如果b为null或者长度为0则直接返回包装后的字符串
	 */
	public static String encode(byte[] b) {
		if (null == b || b.length == 0) {
			return b != null ? new String(b) : null;
		}
		return org.springframework.util.Base64Utils.encodeToString(b);
	}

	/**
	 * 对字符串进行base64解码,返回结果为字符数组
	 * 
	 * @param s
	 *            需要解码的字符串
	 * @return 解码后的字符数组
	 */
	public static byte[] decode(String s) {
		if (StringUtils.isNullOrEmpty(s)) {
			return s != null ? s.getBytes() : null;
		}
		return org.springframework.util.Base64Utils.decodeFromString(s);
	}

	/**
	 * 对字符创进行base64解码，返回结果为字符串
	 * 
	 * @param s
	 *            需要解码的字符串
	 * @return 解码后的字符串结果
	 */
	public static String decodeStr(String s) {
		if (StringUtils.isNullOrEmpty(s)) {
			return s;
		}
		return new String(org.springframework.util.Base64Utils.decodeFromString(s));
	}
}
