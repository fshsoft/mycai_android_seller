package com.yizan.community.business.util;

import android.text.TextUtils;

public class CheckUtils {
	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {

		String telRegex = "[1][35789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}
}
