package com.fanwe.seallibrary.model.result;

import java.util.List;

/**
 * 订单简要实体
 * Created by atlas on 15/9/23.
 * Email:atlas.tufei@gmail.com
 */
public class OrderSummary{
    public int id;
    public String shopName;
    public String totalFee;
    public String freight;
    //支付状态
    public String payStatusStr;
    //是否已经完成 (用于区分订单列表显示的颜色,真为红色,假为灰色)
    public boolean isFinished;
    public String orderStatusStr;
    public String createTime;
    public double sellerFee;
    public double payFee;
}
