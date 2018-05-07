package com.yt.pay.wxpay.http.impl;

import java.util.Iterator;
import java.util.Map.Entry;

import com.yt.pay.wxpay.http.IRequestParamsFormat;

public class XmlRequestParamsFormat implements IRequestParamsFormat {

	private String sign;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String formatToStr(Iterator it) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v)) {
				sb.append("<" + k + ">");
				sb.append(v);
				sb.append("</" + k + ">");
			}
		}
		sb.append("<sign>");
		sb.append(sign);
		sb.append("</sign>");
		sb.append("</xml>");
		return sb.toString();
	}

}
