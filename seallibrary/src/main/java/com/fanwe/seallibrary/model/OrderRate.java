package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

/**
 * 评价实体
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-4-2 上午10:31:16
 */
public class OrderRate implements Serializable {



	public int id;
	public UserInfo user;
	// 评价内容
	public String content;
	// 卖家回复
	public String reply;
	// 图片数组
	public List<String> images;
	// 专业得分1-5分
	public int specialtyScore;
	// 沟通得分1-5分
	public int communicateScore;
	// 守时得分1-5分
	public int punctualityScore;
	// 得分，三个得分的平均分
	public float score;
	// good：好评neutral：中评 bad：差评
	public String result;
	public long createTime;

	public OrderInfo order;
	public long replyTime;

	public Goods goods;

	public String sellerReply;
	public String sellerReplyTime;


}
