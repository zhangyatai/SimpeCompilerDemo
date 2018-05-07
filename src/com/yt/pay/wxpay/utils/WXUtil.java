

package com.yt.pay.wxpay.utils;

import java.util.Random;

public class WXUtil {

	//获取随机字符串
	public static String getNonceStr() {
		// Random random = new Random();
		// return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)),
		// "UTF-8");
		Random random = new Random();
		return MD5Util.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	public static String getTimeStamp() {
		String time = String.valueOf(System.currentTimeMillis() / 1000);
		System.out.println("时间戳: " + time);
		return time;
	}
}
