package com.fiftyonemycai365.seller.business.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-20 上午10:19:45
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final int DATABASEVERSION = 1;// 数据库版本
	private static DBHelper sDBHelper = null;
	// 数据库名称常量
	private final static String DATABASENAME = "o2o.db"; // 数据库名称

	public static DBHelper getDBHelper(Context context) {
		if (sDBHelper == null) {
			sDBHelper = new DBHelper(context);
		}
		return sDBHelper;
	}

	public DBHelper(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}