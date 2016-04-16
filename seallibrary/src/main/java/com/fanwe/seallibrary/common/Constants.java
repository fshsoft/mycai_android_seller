package com.fanwe.seallibrary.common;

public class Constants {
    public static final String OSS_FILE_PATH = "/temp/";
    public static final String TOKEN = "token";
    public static final String TOKEN_LOGIN = "logintoken";
    public static final String STAFF_ID = "staffId";
    public static final String USER_ID = "userId";
    public static final String DEVICE_TYPE = "android";
    public static final String PREFERENCE_CONFIG = "config";
    public static final String SHOP_INFO = "shop";
    public static final String KEY = "key";
    //商家
    public static final int ROLE_SELLER=1;
    //配送人员
    public static final int ROLE_STAFF=2;
    //服务人员
    public static final int ROLE_SERVICE=4;
    /**
     * 分页，查询所有
     **/
    public static final int PAGE_SIZE = 20;
    public static final String DATA = "data";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DATA = "data";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_CHOICE_MODE = "singleChoice";
    public static final String EXTRA_STATUS = "status";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_BACK_ABLE = "backable";
    public static final String EXTRA_INDEX = "index";
    public static final String EXTRA_POINT = "point";
    public static final String EXTRA_KEYS = "keywords";
    public static final String EXTRA_SHOP = "shopinfo";

    public static final int HEADER = 0;
    public static final int ITEM = 1;
    public static final int ITEM1 = 1;
    public static final int ITEM2 = 2;
    //订单类型-商品
    public static final int ORDER_TYPE_GOODS = 1;
    //订单类型-服务
    public static final int ORDER_TYPE_SERVICE = 2;

    public static final String PREFERENCE_SERVICE_TEL = "service_tel";

    public static final String PATTERN_Y_M = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_Y_M_H_M = "yyyy年MM月dd日 HH:mm";
    public static final String PATTERN_H_M = "HH:mm";

    public static final String YMD = "yyyy-MM-dd";
    public static final String MMSS = "mm:ss";

    /**
     * 订单ID
     */
    public static final String ORDERID = "orderId";

    /**
     * 月份
     */
    public static final String MONTH = "month";
    /**
     * 评价百分比
     **/
    public static final String PATTERN_PERCENT_EVALUATION = "0.0%";

    public static final String WEEKS = "weeks";
    public static final String ALLWEEKS = "allweeks";
    public static final String WEEKS_TIME = "weekstime";


    public interface GOODS_TYPE {
        int GOODS = 1;
        int SERVICE = 2;
    }

    // 微信支付
    public static final String ACTION_PAY_RESULT = "com.yizan.community.business.WXPAY_RECEIVED";

    public static final String PAY_TYPE_WEICHAT = "weixin";
    public static final String PAY_TYPE_ALIPAY = "alipay";

}
