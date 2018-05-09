package com.yt.pay.wxpay;

import com.google.gson.Gson;
import com.yt.pay.wxpay.http.bean.OrderResult;
import com.yt.pay.wxpay.http.impl.PrepayIdRequestHandler;
import com.yt.pay.wxpay.utils.ConstantUtil;
import com.yt.pay.wxpay.utils.OrderUtils;
import com.yt.pay.wxpay.utils.WXUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

//支付接口
// WxPayServlet

//接口名称
//name:商品名称
//price:商品价格

public class WxPayServlet extends HttpServlet {

    //首先：定义支付应用ID、商户ID

    private static String APP_ID = ConstantUtil.APP_ID;
    private static String PARATENR_ID = ConstantUtil.PARTNER_ID;
    private static String PARTNER_KEY = ConstantUtil.PARTNER_KEY;
    //统一下单接口 （微信支付服务器提供的）
    public static String URL_UNIFIEDORDER = ConstantUtil.URL_UNIFIEDORDER;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         String name = req.getParameter("name");
        String price = req.getParameter("price");
        this.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //第一步-签名订单信息

        //第一点：获取客户端传过来的参数
         req.setCharacterEncoding("UTF-8");
         resp.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String price = req.getParameter("price");

        //第二点：设置访问微信支付服务器请求数据类型
        resp.reset();
        resp.setHeader("ContentType", "text/xml");

        //第三点：创建请求微信支付服务器参数的集合

        //做了请求的封装
        PrepayIdRequestHandler handler = new PrepayIdRequestHandler(req, resp);
        //统一下单的接口(调用微信支付服务器需要的接口)
        handler.setGateUrl(ConstantUtil.URL_UNIFIEDORDER);
        //设置应用的密钥
        handler.setKey(PARTNER_KEY);
        //设置应用AppID
        handler.setParameter("appid", APP_ID);
        //商户号
        handler.setParameter("mch_id", PARATENR_ID);
        //随机字符串
        handler.setParameter("nonce_str", WXUtil.getNonceStr());

        //商品描述
        handler.setParameter("body", name);
        //商户订单号
        String out_trade_no = OrderUtils.getOrderNumber();
        handler.setParameter("out_trade_no", out_trade_no);
        //总金额
        handler.setParameter("total_fee", price);
        //终端IP(客户端IP)
        handler.setParameter("spbill_create_ip", req.getRemoteAddr());
        //通知URL(微信支付服务器回调商户服务器的接口)
        handler.setParameter("notify_url", ConstantUtil.NOTIFY_URL);
        //交易类型
        handler.setParameter("trade_type", "APP");

        //第四点：对订单信息进行签名
        String sign = handler.createMD5Sign();
        handler.setParameter("sign", sign);


        //第二步-调用微信统一下单(获取prepay_id)

        OrderResult orderResult = new OrderResult();
        try {
            //返回结果是一串XML字符串
            Map paramsMap = handler.sendPrepay();
            String prepay_id = paramsMap.get("prepay_id").toString();

            if (prepay_id != null && !"".equals(prepay_id)) {
                //统一下单接口调用成功
                //第三步-进行二次签名
                //调起支付接口
                //请求列表集合（清空:不影响新的参数的签名）
                handler.clear();

                String noncestr = paramsMap.get("noncestr").toString();
                String timestamp = WXUtil.getTimeStamp();
                //密钥
                handler.setKey(PARTNER_KEY);
                //设置应用ID
                handler.setParameter("appid", APP_ID);
                //预付单ID
                handler.setParameter("prepayid", prepay_id);
                //商户ID
                handler.setParameter("partnerid", PARATENR_ID);
                //扩展字段
                handler.setParameter("package", "Sign=WXPay");
                //随机字符串
                handler.setParameter("noncestr", noncestr);
                //时间戳
                handler.setParameter("timestamp", timestamp);
                //进行二次签名（与第一次签名参数不同）
                //第一次签名是对订单信息签名，获取prepay_id
                //第二次签名 是对支付信息进行签名
                sign = handler.createMD5Sign();

                //采用json解析
                //构建返回客户端的数据
                OrderResult.OrderBean orderBean = new OrderResult.OrderBean();
                orderBean.setAppid(APP_ID);
                orderBean.setNoncestr(noncestr);
                orderBean.setPackageValue("Sign=WXPay");
                orderBean.setPartnerid(PARATENR_ID);
                orderBean.setPrepayid(prepay_id);
                orderBean.setTradeType(paramsMap.get("trade_type").toString());
                orderBean.setSign(sign);
                orderBean.setTimestamp(timestamp);
                orderResult.setOrderBean(orderBean);

                //将实体类转换成json数据格式
                orderResult.setResultCode(0);
                orderResult.setResultMessage("获取成功!");

            }else {
                //统一下单失败
                orderResult.setResultCode(9);
                orderResult.setResultMessage("统一下单失败！");
            }
        } catch (Exception e) {
            orderResult.setResultCode(6);
            orderResult.setResultMessage("位置错误");
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String jsonStr = gson.toJson(orderResult);
        //第四步-把签名信息返回给客户端
        resp.getWriter().print(jsonStr);

    }
}
