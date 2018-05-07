
package com.yt.pay.wxpay.http.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yt.pay.wxpay.http.IRequestParamsFormat;
import com.yt.pay.wxpay.http.RequestHandler;
import com.yt.pay.wxpay.http.client.TenpayHttpClient;
import com.yt.pay.wxpay.utils.MD5Util;
import com.yt.pay.wxpay.utils.Sha1Util;
import com.yt.pay.wxpay.utils.XMLUtil;

public class PrepayIdRequestHandler extends RequestHandler {

	private String sign;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public PrepayIdRequestHandler(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	/**
	 * 创建签名SHA1
	 * 
	 * @param signParams
	 * @return
	 * @throws Exception
	 */
	public String createSHA1Sign() {
		IRequestParamsFormat paramsFormat = new UrlRequestParamsFormat();
		String params = paramsFormat.formatToStr(getAllParameters().entrySet().iterator());
		String appsign = Sha1Util.getSha1(params);
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "sha1 sb:" + params);
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "app sign:" + appsign);
		return appsign;
	}

	/**
	 * md5签名
	 * 
	 * @return
	 */
	public String createMD5Sign() {
		IRequestParamsFormat paramsFormat = new UrlRequestParamsFormat();
		//拼接: key=value&key1=value1...
		String params = paramsFormat.formatToStr(getAllParameters().entrySet().iterator());
		if (null != getKey() && !"".equals(getKey())) {
			params = params + "&key=" + getKey();
		}
		System.out.println("签名前:" + params);
		//签名订单信息
		String appsign = MD5Util.getMessageDigest(params.getBytes());
		return appsign;
	}

	// 提交预支付
	public Map sendPrepay() throws Exception {
		//将订单信息拼接xml(按照顺序进行拼接xml文件格式)
		//例如：<xml><appid>wefevrvr</appid></xml>
		XmlRequestParamsFormat paramsFormat = new XmlRequestParamsFormat();
		paramsFormat.setSign(getSign());
		String params = paramsFormat.formatToStr(getAllParameters().entrySet().iterator());

		System.out.println("提交服务器信息：" + params);

		String requestUrl = super.getGateUrl();
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "requestUrl:" + requestUrl);
		TenpayHttpClient httpClient = new TenpayHttpClient();
		httpClient.setReqContent(requestUrl);
		String resContent = "";
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "post data:" + params);
		if (httpClient.callHttpPost(requestUrl, params)) {
			resContent = httpClient.getResContent();
			System.out.println("微信返回内容：" + resContent);
			if (resContent.indexOf("prepay_id") > 0) {
				return XMLUtil.doXMLParse(resContent);
			}
			this.setDebugInfo(this.getDebugInfo() + "\r\n" + "resContent:" + resContent);
		}
		return null;
	}

}