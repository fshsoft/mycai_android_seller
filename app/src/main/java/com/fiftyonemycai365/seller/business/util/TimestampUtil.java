package com.fiftyonemycai365.seller.business.util;

import android.text.TextUtils;

import com.zongyou.library.util.DateUtil;

public class TimestampUtil
{
	/**
	 * 获取时间格式化后的字符串
	 * @param unixTimestamp unix时间戳
	 * @param format 格式化规则
	 * @param defaultStr 格式化失败的默认值
	 * @return
	 */
	public static String getFormatString(String unixTimestamp, String format, String defaultStr)
	{
		String returnValue = defaultStr == null ? "" : defaultStr;
		try
		{
			if (!TextUtils.isEmpty(unixTimestamp))
			{
				long milliseconds = Long.valueOf(unixTimestamp) * 1000;
				returnValue = DateUtil.UTC2GMT(format, milliseconds);
			}
		} catch (NumberFormatException e)
		{

		}
		return returnValue;
	}

	/**
	 * 获取时间格式化后的字符串
	 * @param unixTimestamp unix时间戳
	 * @param format 格式化规则
	 * @return
	 */
	public static String getFormatString(String unixTimestamp, String format)
	{
		return getFormatString(unixTimestamp, format, "");
	}

	/**
	 * 获取当前时间的格式化字符串
	 * @param format 格式化规则
	 * @param defaultStr 默认值
	 * @return
	 */
	public static String getCurrentFormatString(String format, String defaultStr)
	{
		String returnValue = defaultStr == null ? "" : defaultStr;
		long milliseconds = Long.valueOf(System.currentTimeMillis());
		returnValue = DateUtil.UTC2GMT(format, milliseconds);
		return returnValue;
	}

	/**
	 * 获取当前时间的格式化字符串
	 * @param format 格式化规则
	 * @return
	 */
	public static String getCurrentFormatString(String format)
	{
		return getCurrentFormatString(format, "");
	}

	/**
	 * 获取当前时间的格式化字符串
	 * @param unixTimestamp 服务器返回的UNIX时间戳，单位秒，没有加8
	 * @return
	 */
	public static String getFormatStringJia8(long unixTimestamp)
	{
		long timestamp = unixTimestamp + 8 * 3600;
		return DateUtil.toStrTime(timestamp * 1000);
	}
}
