package com.fanwe.seallibrary.model;

import java.io.Serializable;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-06
 * Time: 16:53
 * 功能:
 */
public class PaymentInfo implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public String code; // 支付代码
    public String name;
    public String icon;
    public int isDefault; // 1：是
    public String content; // 支付说明

    public boolean bSel;
}