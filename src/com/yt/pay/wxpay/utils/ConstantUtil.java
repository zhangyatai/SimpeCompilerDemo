
package com.yt.pay.wxpay.utils;


public class ConstantUtil {

	// 微信开发平台应用id
	public static String APP_ID = "";
	// 财付通商户号
	public static String PARTNER_ID = "";
	// 商户号对应的密钥
	public static String PARTNER_KEY = "";
	// 统一下单
	public static String URL_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 接收财付通通知的URL
	public static String NOTIFY_URL = "http://localhost:8080/Dream_PayServer/payNotifyUrl.jsp";
}
