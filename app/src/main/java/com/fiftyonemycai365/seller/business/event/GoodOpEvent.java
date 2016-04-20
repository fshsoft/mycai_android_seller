package com.fiftyonemycai365.seller.business.event;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-11-06
 * Time: 15:02
 * FIXME
 */
public class GoodOpEvent {
    public Object viewObj;
    public int op;
    public boolean addNew;

    public GoodOpEvent(Object viewObj, int op){
        this.viewObj = viewObj;
        this.op = op;
        this.addNew = false;
    }

    public GoodOpEvent(Object viewObj, boolean addNew){
        this.viewObj = viewObj;
        this.op = 0;
        this.addNew = addNew;
    }
}
