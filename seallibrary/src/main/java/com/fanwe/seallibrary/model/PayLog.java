package com.fanwe.seallibrary.model;

import com.fanwe.seallibrary.model.request.WeixinPayRequest;

import java.io.Serializable;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-06
 * Time: 17:18
 * 功能:
 */
public class PayLog implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public int id;
    public String sn;
    public double money;
    //weixin|weixinJs|alipay|alipayWap
    public String paymentType;
    public WeixinPayRequest payRequest;
}
