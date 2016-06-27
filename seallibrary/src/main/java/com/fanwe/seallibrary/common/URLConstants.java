package com.fanwe.seallibrary.common;


import com.fanwe.seallibrary.BuildConfig;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 下午7:08:16
 */
public class URLConstants {


    public static final URLEnum ENUM = URLEnum.NoEncryption;
    // public static final String DOMAIN =
    // "http://api2.vso2o.jikesoft.com/seller/v1/";
//    public static final String DOMAIN = "http://api."+BuildConfig.DOMAIN_WWW+"/staff/v1/";
    public static final String URL = BuildConfig.DOMAIN_WWW;
    public static final String DOMAIN = "http://" + BuildConfig.DOMAIN_API + URL + "/staff/v1/";
    //FIXME api.
//    public static final String DOMAIN = "http://api." + BuildConfig.DOMAIN_WWW + "/staff/v1/";
//    public static final String DOMAIN = "http://192.168.1.199/staff/v1/";
    public static final String INIT = DOMAIN + "app.init";

    /**
     * POI webservice方式搜索
     */
    public static final String POI_SEARCH="http://apis.map.qq.com/ws/place/v1/suggestion";
    /**
     * geocoder webservice方式搜索
     */
    public static final String MAP_GEOCODER="http://apis.map.qq.com/ws/geocoder/v1";
    public static final String INENTIFYING = DOMAIN + "user.mobileverify";// 发验证码
    public static final String REGISTER = DOMAIN + "user.reg"; // 注册
    public static final String RE_PWD = DOMAIN + "user.repwd"; // 修改密码
    public static final String LOGIN = DOMAIN + "user.login"; // 登录
    public static final String QUICK_LOGIN = DOMAIN + "user.verifylogin"; // 会员快捷登陆（验证码登录）
    public static final String LOGOUT = DOMAIN + "user.logout"; // 登出
    /**
     * 佣金查询
     */
    public static final String COMMISSION_LIST = DOMAIN + "user.commission";
    /**
     * 历史订单
     */
    public static final String STATISTICS_DETAIL = DOMAIN + "statistics.detail";
    /**
     * 订单列表
     */
    public static final String ORDER_LISTS = DOMAIN + "order.lists";
    /**
     * 订单详情
     */
    public static final String ORDER_DETAIL = DOMAIN + "order.detail";
    /**
     * 订单状态改变
     */
    public static final String ORDER_STATUS = DOMAIN + "order.status";
    /**
     * 订单分配服务、配送人员
     */
    public static final String ORDER_ALLOC_STAFF = DOMAIN + "order.designate";
    public static final String ORDERDESTAFFLOG = DOMAIN + "order.stafflog";
    /**
     * 订单更新状态
     */
    public static final String ORDERUPDATESTATUS = DOMAIN + "order.updatestatus";
    /**
     * 开始服务
     */
    public static final String ORDER_STARTSERVICE = DOMAIN + "order.startservice";
    /**
     * 接受订单
     */
    public static final String ORDER_RECEIVESERVICE = DOMAIN + "order.receiveservice";
    /**
     * 拒绝订单
     */
    public static final String ORDER_REFUSESERVICE = DOMAIN + "order.refuseservice";
    /**
     * 评价回复
     */
    public static final String ORDER_STAFF_REPLY = DOMAIN + "rate.staff.reply";
    /**
     * 正在服务的订单
     */
    public static final String ORDERFIRST = DOMAIN + "order.first";
    /**
     * 统计明细
     */
    public static final String STATISTICSDETAIL = DOMAIN + "statistics.detail";
    /**
     * 按月份来统计
     */
    public static final String STATISTICSMONTH = DOMAIN + "statistics.month";
    //员工余额
    public static final String STAFFBALANCE = DOMAIN + "staff.get";
    // 评价列表
    public static final String EVALUATION_LIST = DOMAIN + "rate.staff.lists";
    // 评价统计
    public static final String EVALUATION_STATISTICS = DOMAIN + "staff.detail";
    // 日程列表
    public static final String SCHEDULE_LIST = DOMAIN + "schedule.lists";
    // 日程列表
    public static final String SCHEDULE_UPDATE = DOMAIN + "schedule.update";
    // 消息列表
    public static final String MSG_LIST = DOMAIN + "msg.lists";
    // 消息阅读
    public static final String MSG_READ = DOMAIN + "msg.read";
    // 消息删除
    public static final String MSG_DEL = DOMAIN + "msg.delete";
    //是否有未读消息
    public static final String MSG_GET = DOMAIN + "msg.get";


    /**
     * 意见反馈
     */
    public static final String FEEDBACK_CREATE = DOMAIN + "feedback.create";
    // 消息标记
    public static final String MSG_STATUS = DOMAIN + "user.msgStatus";

    /**
     * 添加请假
     */
    public static final String STAFFLEAVE_CREATE = DOMAIN + "staffleave.create";
    /**
     * 请假列表
     */
    public static final String STAFFLEAVE_LISTS = DOMAIN + "staffleave.lists";
    /**
     * 删除请假记录
     */
    public static final String STAFFLEAVE_DELETE = DOMAIN + "staffleave.delete";
    // 员工信息更新
    public static final String STAFF_UPDATE = DOMAIN + "user.info.update";
    /**
     * 是否开启接单
     */
    public static final String USER_ORDER_ACCEPT = DOMAIN + "user.open.status";
    // 服务地址更新
    public static final String STAFF_ADDRESS_ADD = DOMAIN + "staff.address";
    /**
     * 服务时间列表
     */
    public static final String STAFFSTIME_LISTS = DOMAIN + "staffstime.lists";
    /**
     * 员工服务时间详情
     */
    public static final String STAFFSTIME_EDIT = DOMAIN + "staffstime.edit";
    /**
     * 员工服务时间添加
     */
    public static final String STAFFSTIME_ADD = DOMAIN + "staffstime.add";
    /**
     * 员工服务时间删除
     */
    public static final String STAFFSTIME_DELETE = DOMAIN + "staffstime.delete";
    /**
     * 员工服务时间更新
     */
    public static final String STAFFSTIME_UPDATE = DOMAIN + "staffstime.update";
    /**
     * 开始洗车
     */
    public static final String ORDER_START_CAR = DOMAIN + "order.startcar";
    /**
     * 结束洗车
     */
    public static final String ORDER_FINISH_CAR = DOMAIN + "order.finishcar";
    public static final String STAFF_REPLY = DOMAIN + "rate.staff.reply";
    /**
     * 消息
     */

    public static final String MESSAGE_LIST = DOMAIN + "msg.lists";
    public static final String MESSAGE_GETDATA = DOMAIN + "msg.getdata";
    public static final String MESSAGE_READ = DOMAIN + "msg.read";
    public static final String MESSAGE_DELETE = DOMAIN + "msg.delete";
    public static final String MESSAGE_STATUS = DOMAIN + "msg.status";
    /**
     * 未完成定单日程
     */
    public static final String ORDER_SCHEDULE = DOMAIN + "order.schedule";
    /**
     * 历史定单日程
     */
    public static final String ORDER_HISTORY_SCHEDULE = DOMAIN + "order.wapsorderlists";
    /**
     * 家政开始服务
     */
    public static final String ORDER_HOUSE_KEEPING_START = DOMAIN + "order.startservice";
    /**
     * 取件成功
     */
    public static final String ORDER_PICK_UP = DOMAIN + "order.pickup";
    /**
     * 更新定单状态
     */
    public static final String ORDER_UPDATE_STATUS = DOMAIN + "order.updatestatus";
    /**
     * 获取收费商品
     */
    public static final String ORDER_GOODSLISTS = DOMAIN + "order.goodslists";
    /**
     * 更新手机号码校验
     */
    public static final String CHECKMOBILE = DOMAIN + "user.info.verifymobile";
    /**
     * 更新手机号码
     */
    public static final String REFLASHMOBILE = DOMAIN + "user.info.mobile";
    /**
     * 创建订单
     */
    public static final String ORDER_CREATE = DOMAIN + "order.create";
    private static final String DOMAIN_WAP = "http://staff." + BuildConfig.DOMAIN_WWW;
    // 免责声明
    public static final String URL_DISCLAIMER = DOMAIN_WAP + "/More/detail?code=1";
    // 关于我们
    public static final String URL_ABOUT = DOMAIN_WAP + "/More/staffaboutus";
    // 使用帮助
    public static final String URL_HELP = DOMAIN_WAP + "/More/detail?code=2";
    /**
     * 服务商圈设置 Web地址
     */
    public static final String DISTRICT_RANGE = DOMAIN_WAP + "/district/index?token=%s&staffId=%d";


    // 商品详情描述编辑
    public static final String GOOD_DETAIL_EDIT = DOMAIN + "/goods.detail?token=%s&userId=%d&id=%d";

    public static enum URLEnum {

        /**
         * 加密
         */
        Encryption,
        /**
         * 未加密
         */
        NoEncryption,
    }

    /**
     * 商品、服务分类列表
     */
    public static final String GOODS_CATES_LIST = DOMAIN + "goodscate.lists";
    /**
     * 商品、服务分类排序
     */
    public static final String GOODS_CATES_SORT = DOMAIN + "goodscate.sort";
    /**
     * 商品、服务分类删除
     */
    public static final String GOODS_CATES_DEL = DOMAIN + "goodscate.del";
    /**
     * 商品、服务分类编辑
     */
    public static final String GOODS_CATES_EDIT = DOMAIN + "goodscate.edit";
    /**
     * 商品、服务列表
     */
    public static final String GOODS_LIST = DOMAIN + "goods.lists";
    /**
     * 商品、服务编辑
     */
    public static final String GOODS_EDIT = DOMAIN + "goods.edit";
    /**
     * 商品、服务操作（上架、下架、删除）
     */
    public static final String GOODS_OP = DOMAIN + "goods.op";

    /**
     * 经营类型列表
     */
    public static final String SELLER_TRADE = DOMAIN + "seller.trade";
    /**
     * 店铺信息
     */
    public static final String SHOP_INFO = DOMAIN+"shop.info";
    /**
     * 店铺编辑
     */
    public static final String SHOP_EDIT = DOMAIN+"shop.edit";

    /**
     * 商家账单查询
     */
    public static final String SHOP_ACCOUNT = DOMAIN + "shop.account";
    /**
     * 商家银行账户信息
     */
    public static final String USER_BANKINFO = DOMAIN + "user.bankinfo";

    /**
     * 商家提现
     */
    public static final String USER_WIDTHDRAW = DOMAIN + "user.withdraw";
    /**
     * 评价列表
     */
    public static final String SELLER_EVALIST = DOMAIN + "seller.evalist";
    /**
     * 评价回复
     */
    public static final String SELLER_EVAREPLY = DOMAIN + "seller.evareply";

    public static final String SELLERMAP = DOMAIN + "shop.sellermap";


    /**
     * 服务、配送人员列表
     */
    public static final String STAFF_LIST = DOMAIN + "order.stafflist";

    /**
     * 经营分析 URL地址
     */
    public static final String ORDER_STATISTIC = DOMAIN + "order.statistics?token=%s&userId=%d";

    /**
     * 店铺对账URL地址
     */
    public static final String SHOP_RECONCILIATION = DOMAIN + "order.cateincome?token=%s&userId=%d";

    // 删除银行账户信息
    public static final String USER_BANK_DEL = DOMAIN + "seller.delbankinfo";
    // 商家添加/编辑银行卡信息
    public static final String USER_BANK_EDIT = DOMAIN + "seller.savebankinfo";
    // 商家获取银行卡信息
    public static final String USER_BANK_INFO = DOMAIN + "seller.getbankinfo";

    //申请充值
    public static final String SELLER_RECHARGE = DOMAIN + "seller.recharge";

}
