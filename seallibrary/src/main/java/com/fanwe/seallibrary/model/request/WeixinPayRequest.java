package com.fanwe.seallibrary.model.request;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-06
 * Time: 17:13
 * 功能:
 */
public class WeixinPayRequest {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //商户号
    public String partnerid;
    //预支付交易会话标识
    public String prepayid;
    //扩展字段
    public String packages;
    //随机字符串
    public String noncestr;
    //时间戳
    public String timestamp;
    //签名
    public String sign;
}
