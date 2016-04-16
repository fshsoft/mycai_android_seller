package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class MessageInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 622452462272939951L;
    public int id; //Id
    public String title; //标题
    public String content; // 内容
    public String createTime; //时间
    public String args; // 参数
    public int type; // 消息类型1：普通消息 2：html页面，args为url 3：订单消息，args为订单id 4：佣金消息,点击进入佣金界面

    public int isRead; // 阅读状态 0：已读 1：未读
    public int  createType;//1:平台，2:商家

    public boolean  isChick=false;
}
