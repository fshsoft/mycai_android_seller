package com.fiftyonemycai365.seller.business.util;

import java.io.File;
import java.util.Date;

import android.content.Context;

import com.aliyun.mbaas.oss.OSSClient;
import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.AccessControlList;
import com.aliyun.mbaas.oss.storage.OSSBucket;
import com.aliyun.mbaas.oss.storage.OSSFile;
import com.aliyun.mbaas.oss.storage.TaskHandler;
import com.aliyun.mbaas.oss.util.OSSToolKit;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.ConfigInfo;
import com.fanwe.seallibrary.model.OSSInfo;
import com.zongyou.library.util.DateUtil;
import com.zongyou.library.util.FileType;
import com.zongyou.library.util.storage.PreferenceUtils;

public class OSSUtils {
	public static OSSInfo sOSSInfo = null;
	public static void init(Context context) {
		OSSClient.setApplicationContext(context);
		ConfigInfo configInfo = PreferenceUtils.getObject(context, ConfigInfo.class);
		if(configInfo != null && configInfo.oss != null) {
			sOSSInfo = configInfo.oss;
			OSSToolKit.generateToken(configInfo.oss.access_id, configInfo.oss.access_key, "");
			OSSClient.setGlobalDefaultHostId(configInfo.oss.host);
			OSSClient.setGlobalDefaultACL(AccessControlList.PUBLIC_READ_WRITE);
		}
	}

	/**
	 * 保存图片
	 * 
	 * @param filePath
	 *            本地文件路径名 /mnt/sdcard/800.png
	 * @param callback
	 */
	public static TaskHandler save(String filePath, SaveCallback callback) {
		OSSBucket bucket = new OSSBucket(sOSSInfo.bucket);
		String[] names = filePath.split(File.separator);
		if (null != names && names.length > 1) {
			OSSFile ossFile = new OSSFile(bucket, generateObjectKey(names[names.length - 1]));
			ossFile.setUploadFilePath(filePath, FileType.getFileType(filePath)); // 指定要上传的文件,和文件
																					// 内容的类型
			return ossFile.ResumableUploadInBackground(callback);
		} else
			return null;
	}

	/**
	 * 生成objeckkey
	 * 
	 * @param fileName
	 * @return
	 */
	public static String generateObjectKey(String fileName) {
		String tmp = "";
		try{
			tmp=fileName.substring(fileName.lastIndexOf("."));
		}catch (Exception e){
			tmp = fileName;
		}
		StringBuilder sb = new StringBuilder(Constants.OSS_FILE_PATH);
		sb.append(DateUtil.getCurrentDateString()).append(File.separator).append(new Date().getTime()).append(tmp);
		return sb.toString();
	}
}