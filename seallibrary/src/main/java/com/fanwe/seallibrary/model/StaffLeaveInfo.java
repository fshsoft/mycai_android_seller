package com.fanwe.seallibrary.model;

/**
 * Created by admin on 2015/10/29.
 */
public class StaffLeaveInfo {
    //请假记录编号
    public int id;
    //请假开始时间
    public String beginTime;
    //请假结束时间
    public String endTime;
    //请假理由
    public String remark;
    //请假创建时间
    public String createTime;
    //1 :同意  0:待审核 -1: 驳回
    public int status;
    //请假状态字符串
    public String statusStr;
}
