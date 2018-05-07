package com.yt.pay.wxpay.http.impl;

import java.util.Iterator;
import java.util.Map.Entry;

import com.yt.pay.wxpay.http.IRequestParamsFormat;

public class UrlRequestParamsFormat implements IRequestParamsFormat {

	@Override
	public String formatToStr(Iterator it) {
		StringBuffer sb = new StringBuffer();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append(k + "=" + v + "&");
		}
		return sb.substring(0, sb.lastIndexOf("&"));
	}

}
