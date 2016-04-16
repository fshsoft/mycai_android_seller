package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author haojiahe
 * @time 2015-3-23下午8:18:00
 */
public class ScheduleDayInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public List<OrderInfo> list;
    public List<TimeInfo> times;
    public String date;

    public static class TimeInfo {
        public String time;
        public String week;
        public long date;
        public boolean isSelected;
    }

    // TODO: 不需要拉
    public String day;
    public String week;
}
