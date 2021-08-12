package com.qf.my_shop_service_pay.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AlipayController {

    private String order_no;
    private Long total_amount;//单位：分


    @RequestMapping("notify")
    @ResponseBody
    public Map<String,Object> myNotify(HttpServletRequest request){
        System.out.println("======="+this.order_no+"============");
        //响应的map
        Map<String,Object> response_map = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> paramsMap = new HashMap<String, String>();  //将异步通知中收到的所有参数都存放到 map 中
        //map中参数类型转换
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            StringBuilder strings = new StringBuilder();
            String[] values = entry.getValue();
            for (int i = 0; i < values.length; i++) {
                if(i>0){
                    strings.append(",").append(values[i]);
                }else{
                    strings.append(values[i]);
                }
            }
            paramsMap.put(entry.getKey(),strings.toString());
        }

        //验签
        boolean  signVerified = false;  //调用SDK验证签名
        try {
            //验签：本次的该接口的调用，是否是支付宝调用的？
            signVerified = AlipaySignature.rsaCheckV1(paramsMap,
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr7I8h57pi070PqJzcrl2gIm0K3Of5UX0UcuSg2BnsSIdLkTP72sxYJpfc/BJcx9Ae7XDLQNha6Ufy46M4sxXTVnbIX1SwXb1SrAh50HKo4ndF7uiAe9Z4QvjXIJGzbNTPVi3T0UctB9sB4JOYu0a5nm4jlfhaSAeyMkWYylt/bPnpmhGDi9Yqd1XVQjy1/SDrUa+Z+fRGAhcM0UKRPRifaH8UKg0/wGnS5jGPBmlYrBOnK+ZLW9CdzkHrUOWF6UB/SB7mHx8JhhvQHB+CLPElc2psZ4kGcv/aI31Rp6wqQa8X2GG3E1aunCi8UOttR2E+NMsea5P5P0X63igXLyNyQIDAQAB",//支付宝公钥
                    "utf-8",
                    "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (signVerified){
            // 验签确实是支付宝在调用，成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            //验签此次支付的信息是否真实。
            //订单id是否正确
            //订单金额是否正确
            System.out.println(paramsMap);
            String out_trade_no = paramsMap.get("out_trade_no");
            if(this.order_no.equals(out_trade_no)){
                //再校验金额
                if ( paramsMap.get("total_amount").equals(this.total_amount)) {
                    System.out.println("修改订单状态为：已支付。");
                    //成功的回信
                    Map<String,String> map1 = new HashMap<String,String>();
                    map1.put("code","10000");
                    map1.put("msg","Success");
                    map1.put("trade_no",paramsMap.get("trade_no"));
                    map1.put("out_trade_no",paramsMap.get("out_trade_no"));
                    map1.put("seller_id",paramsMap.get("seller_id"));
                    map1.put("total_amount",paramsMap.get("total_amount"));
                    map1.put("merchant_order_no",paramsMap.get("merchant_order_no"));
                    response_map.put("alipay_trade_page_pay_response",map1);
                    response_map.put("sign","MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr7I8h57pi070PqJzcrl2gIm0K3Of5UX0UcuSg2BnsSIdLkTP72sxYJpfc/BJcx9Ae7XDLQNha6Ufy46M4sxXTVnbIX1SwXb1SrAh50HKo4ndF7uiAe9Z4QvjXIJGzbNTPVi3T0UctB9sB4JOYu0a5nm4jlfhaSAeyMkWYylt/bPnpmhGDi9Yqd1XVQjy1/SDrUa+Z+fRGAhcM0UKRPRifaH8UKg0/wGnS5jGPBmlYrBOnK+ZLW9CdzkHrUOWF6UB/SB7mHx8JhhvQHB+CLPElc2psZ4kGcv/aI31Rp6wqQa8X2GG3E1aunCi8UOttR2E+NMsea5P5P0X63igXLyNyQIDAQAB");
                }
                System.out.println("检验失败");
            }else{
                System.out.println("检验失败");
                //失败的回信
                Map<String,String> map1 = new HashMap<String,String>();
                map1.put("code","40004");
                map1.put("msg","Business Failed");
                map1.put("trade_no",paramsMap.get("trade_no"));
                map1.put("out_trade_no",paramsMap.get("out_trade_no"));
                map1.put("seller_id",paramsMap.get("seller_id"));
                map1.put("total_amount",paramsMap.get("total_amount"));
                map1.put("merchant_order_no",paramsMap.get("merchant_order_no"));
                response_map.put("alipay_trade_page_pay_response",map1);
                response_map.put("sign","MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr7I8h57pi070PqJzcrl2gIm0K3Of5UX0UcuSg2BnsSIdLkTP72sxYJpfc/BJcx9Ae7XDLQNha6Ufy46M4sxXTVnbIX1SwXb1SrAh50HKo4ndF7uiAe9Z4QvjXIJGzbNTPVi3T0UctB9sB4JOYu0a5nm4jlfhaSAeyMkWYylt/bPnpmhGDi9Yqd1XVQjy1/SDrUa+Z+fRGAhcM0UKRPRifaH8UKg0/wGnS5jGPBmlYrBOnK+ZLW9CdzkHrUOWF6UB/SB7mHx8JhhvQHB+CLPElc2psZ4kGcv/aI31Rp6wqQa8X2GG3E1aunCi8UOttR2E+NMsea5P5P0X63igXLyNyQIDAQAB");
            }
        } else {
        }
        return response_map;
    }


    @RequestMapping("toPay")
    public void toPay(HttpServletResponse httpResponse,String orderNo) throws IOException {
        this.order_no = orderNo;
        String CHARSET = "utf-8";
        ////支付网关  appid   私钥  格式   字符集  支付宝公钥 签名方式
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                "2016102200737832",
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDl3G93bl6sxfcWANkrlVNVflwEDSru/9gy38Ib/b5xoJHdA1GgdMFbRnBGebUG22KQ4f8G0WTvBZV9gMz0lyQ1sNLFpZFdq9X3otQSOMebRR7Uv0wfN0jcQFJbSGXanYSpVPAnGVGBtNEiU8H4FnG4DGAnAH5Q3H/z1eDRPU/CFcVfJ6tT3lMzL8QKzFUlHrn//XreZL8N7uyCcoAx19w+MzqUjqoDtfTQQuO5YidH3a17yzOfeDH8P7WgcSnnm4PpgEgnUbEKNsqBKlVwXg6A8yM6Je+H7/2zq4pv60Bykpz7ihISwDZo/x7xc/ZNGe80rdVLslzX7bsekeh02+aRAgMBAAECggEAOoCoPhVxMzgPcyI0I8uVsiS+9qkDTGEBfbFFjio+1lyvwzidBeGxdLGw0b8YO+6KX6Wlau7QaaVaLt8hSv9Mz/15J6TEIrshci2XHCwnYXVuK2ek0C0Z9jTIwULj6Yg9w3tCxDDFdeW3dK0sqR/NQbKkpkiR3bLBPiYpWhfovNSiIcWgl3frYk+kgt11drHq9f5Y3Yx63fRzddt0T33l/l9U3+7kzLCbS3jdkfe4ZUkHxATk9PWIwLtg/5pXcjBRnwHiBLwYZQhg5Jv3acflndb76sm0zAYZS5XeFFEeaOm0KhOiuysH6RPO2hLZzsEKGvmBo5uro+3voocKUWfdgQKBgQD7CKg1djwxGE2NHAoSa8Lk2WdMKWGIumdX66CI7BjHoEaecJMvMbAEeWOb3kC68WJ9oZYXP3+0gnkAODRS8E8jOeeLP0I3hP73eUDz8+fjxghUkOuhBhIN8NZaURJy71X3jVL1PMgFK24NTx6wNmiFOhQQ9TUIRZsJsIAPDkbqNQKBgQDqaIzuHRqJ0kXuc/Ji1EHmdt2osT8/AaUGvaLI0NMCgJ7W1250IpucK9WwDeGKV6/n0yMrctdAW2K82eHffb99DxErzLWFC2gcAm9GI/JO/EoPGCLcmcPTP/XShvpnNElWbH/ZM5BFjyjUB7mG5I3qtyNVogqp6TpzS5af7pc2bQKBgQDu6RpNZy15/AsylNDxHyXo7w9kaK2ultjr0BCEv04GDv+morUxYTSHzY+DNE6enESEFFjn30MG5HWMQ/FoTdTDTcSTvayjcivGEtxCUIsNN9eNxC68nS5pniAi7K2GPUmvxAyD4Ujrwp2GhRpL0jw/LzsRuIy0Rqp62kPGEGxlUQKBgQDBUoy4bORQOk1WheWnwGrBSdMxp00EB7U0w/2XJTKB49YcVfxQZ6JywGwyEKCsWflHdoiQ8tu3xia+net9SNY+q8h7mQ8ztN6eCxv+evMCVWw22q8VxPMOLLfuc0fdCfBaCI3AcphP5evEQsKuD51DsNrBayzkSOfd4r9NE5c+WQKBgAc6dbgPK5WgU+JjAc/sHjupE9RkwFQkvIzrXybfnwGPG0mRQp8bLq9+GLw9Y8DmTy6Lb4XQ//wpAP7mVmgF9+OkR9nylufgWgym8nS/itgvYk0U8mSISLisY+KBiKD7WpkVbB/yl/jLp7lcoyY7rlGktPUiIWuk0/ZoNd/04d8x",
                "json",
                CHARSET,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr7I8h57pi070PqJzcrl2gIm0K3Of5UX0UcuSg2BnsSIdLkTP72sxYJpfc/BJcx9Ae7XDLQNha6Ufy46M4sxXTVnbIX1SwXb1SrAh50HKo4ndF7uiAe9Z4QvjXIJGzbNTPVi3T0UctB9sB4JOYu0a5nm4jlfhaSAeyMkWYylt/bPnpmhGDi9Yqd1XVQjy1/SDrUa+Z+fRGAhcM0UKRPRifaH8UKg0/wGnS5jGPBmlYrBOnK+ZLW9CdzkHrUOWF6UB/SB7mHx8JhhvQHB+CLPElc2psZ4kGcv/aI31Rp6wqQa8X2GG3E1aunCi8UOttR2E+NMsea5P5P0X63igXLyNyQIDAQAB","RSA2"); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        //设置回调地址
        alipayRequest.setReturnUrl("http://nzk39g.natappfree.cc/pay/success");
        alipayRequest.setNotifyUrl("http://nzk39g.natappfree.cc/notify");//在公共参数中设置回跳和通知地址

        //设置业务参数
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+orderNo+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":10000," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();

    }
}
