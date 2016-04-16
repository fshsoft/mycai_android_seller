package com.fanwe.seallibrary.model;

import com.fanwe.seallibrary.model.result.OrderSummary;

import java.util.List;

/**
 * 订单简要包装对象
 * Created by atlas on 15/10/29.
 */
public class OrderPack {
    public 	int	count;//订单总数
    public double amount;//	double	订单总金额
    public int ingCount	;//	进行中的订单数
    public List<OrderSummary> orders;//订单列表
}
