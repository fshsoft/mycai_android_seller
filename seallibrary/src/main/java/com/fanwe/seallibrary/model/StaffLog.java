package com.fanwe.seallibrary.model;

import java.io.Serializable;

/**
 * 机构员工信息实体
 *
 * @author atlas
 * @email atlas.tufei@gmail.com
 * @time 2015-5-13 上午11:12:14
 */
public class StaffLog implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8114529873373495685L;

    public int id;
    public int orderId;
    public String content;
    public int staffId;
    public int createTime;
}
