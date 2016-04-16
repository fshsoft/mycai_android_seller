package com.fanwe.seallibrary.model;

/**
 * 订单数据统计
 * Created by atlas on 15/10/29.
 */
public class OrderCount {
    public 	int	count;//订单总数
    public double amount;//	double	订单总金额
    public int ingCount	;//	进行中的订单数

    public OrderCount() {
    }
    public OrderCount(OrderPack orderPack){
        if(null ==orderPack)
            return;
        count=orderPack.count;
        amount=orderPack.amount;
        ingCount=orderPack.ingCount;
    }
}
