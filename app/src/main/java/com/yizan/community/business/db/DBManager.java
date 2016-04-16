package com.yizan.community.business.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-20 上午10:45:37
 */
public class DBManager {
	private static SQLiteDatabase mSQLiteDatabase;
	private static final String TAG = "DBManager";

	private static DBManager mInstance;

	public static synchronized DBManager getDBManager(Context context) {
		mInstance = mInstance == null ? new DBManager() : mInstance;
		mSQLiteDatabase = mSQLiteDatabase == null ? DBHelper.getDBHelper(context).getWritableDatabase() : mSQLiteDatabase;
		return mInstance;
	}

	private DBManager() {
		super();
	}

	/**
	 * insert
	 * 
	 * @param persons
	 */
	public long insert(String tabbleName, ContentValues contentValues) {
		if (contentValues.size() <= 0)
			return 0;
		return mSQLiteDatabase.insert(tabbleName, null, contentValues);
	}

	/**
	 * 
	 * @param tabbleName
	 *            表名
	 * @param whereClause
	 *            whereClause表示WHERE表达式，比如“age > ? and age < ?”等
	 * @param whereArgs
	 *            whereArgs参数是占位符的实际参数值
	 * @return 执行成功数据条数
	 */
	public int delete(String tabbleName, String whereClause, String... whereArgs) {
		return mSQLiteDatabase.delete(tabbleName, whereClause, whereArgs);
	}

	/**
	 * 
	 * @param tabbleName
	 *            表名
	 * @param whereClause
	 *            whereClause表示WHERE表达式，比如“age > ? and age < ?”等
	 * @param whereArgs
	 *            whereArgs参数是占位符的实际参数值
	 * @return 执行成功数据条数
	 */
	public int delete(String tabbleName) {
		return mSQLiteDatabase.delete(tabbleName, null, null);
	}

	/**
	 * 
	 * @param tabbleName
	 *            表名
	 * @param contentValues
	 *            ContentValues类型的变量，是键值对组成的Map，key代表列名，value代表该列要插入的值
	 * @param whereClause
	 *            whereClause表示WHERE表达式，比如“age > ? and age < ?”等
	 * @param whereArgs
	 *            whereArgs参数是占位符的实际参数值
	 * @return
	 */
	public int update(String tabbleName, ContentValues contentValues, String whereClause, String... whereArgs) {
		return mSQLiteDatabase.update(tabbleName, contentValues, whereClause, whereArgs);
	}

	/**
	 * 查询表下的所有数据
	 * 
	 * @param tableName
	 *            表明
	 * @param obj
	 * @return
	 */
	public Cursor queryAll(String tableName) {
		return mSQLiteDatabase.query(tableName, null, null, null, null, null, null);
	}

	/**
	 * 查询
	 * 
	 * @param tableName
	 * @param orderBy
	 * @param columns
	 * @return
	 */
	public Cursor queryAllorder(String tableName, String orderBy, String columns[]) {
		return mSQLiteDatabase.query(tableName, columns, null, null, null, null, orderBy);
	}

	/**
	 * 查询表下的所有数据
	 * 
	 * @param tableName
	 *            表明
	 * @param obj
	 * @return
	 */
	public Cursor queryAll(String sql, String[] str) {
		return mSQLiteDatabase.rawQuery(sql, str);
	}

	/**
	 * 分页查询
	 * 
	 * @param tableName
	 *            表名
	 * @param pageNo
	 *            第几页
	 * @param pageSize
	 *            每页显示条数
	 * @return
	 */
	public Cursor queryPage(String tableName, int pageNo, int pageSize) {
		StringBuffer sb = new StringBuffer(((pageNo - 1) * pageSize) == 0 ? "0" : (pageNo - 1) * pageSize + "");
		sb.append(",");
		sb.append(pageSize);
		return mSQLiteDatabase.query(tableName, null, null, null, null, null, null, sb.toString());
	}

	public Cursor queryPage(String tableName, String[] columns, int pageNo, int pageSize) {
		StringBuffer sb = new StringBuffer(((pageNo - 1) * pageSize) == 0 ? "0" : (pageNo - 1) * pageSize + "");
		sb.append(",");
		sb.append(pageSize);
		return mSQLiteDatabase.query(tableName, columns, null, null, null, null, null, sb.toString());
	}

	public Cursor queryPage(String tableName, String[] columns, String where, String[] wheres, int pageNo, int pageSize) {
		StringBuffer sb = new StringBuffer(((pageNo - 1) * pageSize) == 0 ? "0" : (pageNo - 1) * pageSize + "");
		sb.append(",");
		sb.append(pageSize);
		return mSQLiteDatabase.query(tableName, columns, where, wheres, null, null, null, sb.toString());
	}

	/**
	 * 自定义sql语句进行查询
	 * 
	 * @param sql
	 *            sql语句
	 * @param obj
	 *            查询条件值
	 * @return
	 */
	public Cursor queryCustom(String sql, String[] obj) {
		return mSQLiteDatabase.rawQuery(sql, obj);
	}

	/**
	 * 总的数据条数
	 * 
	 * @param tableName
	 *            表明
	 * @return
	 */
	public int count(String tableName) {
		int count = 0;
		Cursor cursor = mSQLiteDatabase.rawQuery("select count(1) from " + tableName, null);
		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		if (cursor != null)
			cursor.close();
		return count;
	}

	/**
	 * 总数据条数
	 * 
	 * @param sql
	 *            sql语句
	 * @param str
	 *            条件
	 * @return
	 */
	public int count(String sql, String... str) {
		int count = 0;
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, str);
		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		if (cursor != null)
			cursor.close();
		return count;
	}

	/**
	 * 
	 * @param tableName
	 *            表明
	 * @param selection
	 *            对查询条件
	 * @param str
	 *            查询条件值
	 * @return
	 */
	public Cursor query(String tableName, String selection, String... str) {
		return mSQLiteDatabase.query(tableName, null, selection, str, null, null, null);
	}

	/**
	 * 返回制定的值 INT
	 * 
	 * @param tableName
	 * @param column
	 * @param where
	 * @param str
	 * @return
	 */
	public int getInt(String tableName, String column, String where, String... str) {
		int count = 0;
		Cursor cursor = mSQLiteDatabase.query(tableName, new String[] { column }, where, str, null, null, null);
		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		if (cursor != null)
			cursor.close();
		return count;
	}

	/**
	 * 返回制定的值 String
	 * 
	 * @param tableName
	 * @param column
	 * @param where
	 * @param str
	 * @return
	 */
	public String getString(String tableName, String column, String where, String... str) {
		String count = "";
		Cursor cursor = mSQLiteDatabase.query(tableName, new String[] { column }, where, str, null, null, null);
		if (cursor.moveToNext()) {
			count = cursor.getString(0);
		}
		if (cursor != null)
			cursor.close();
		return count;
	}

	/**
	 * 
	 * @param tableName
	 *            表明
	 * @param columns
	 *            显示的列名
	 * @param selection
	 *            对查询条件
	 * @param orderBy
	 *            排序
	 * @param str
	 *            查询条件值
	 * @return
	 */
	public Cursor query(String tableName, String[] columns, String selection, String[] str, String orderBy) {
		return mSQLiteDatabase.query(tableName, columns, selection, str, null, null, orderBy);
	}

	/**
	 * 
	 * @param tableName
	 *            表明
	 * @param columns
	 *            查询需要显示的列
	 * @return
	 */
	public Cursor query(String tableName, String[] columns) {
		return mSQLiteDatabase.query(tableName, columns, null, null, null, null, null);
	}

	/**
	 * 判断某张表是否存在
	 * 
	 * @param tabName
	 *            表名
	 * @return
	 */
	public boolean tabbleIsExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			StringBuffer sql = new StringBuffer("select count(*) as c from Sqlite_master  where type ='table' and name ='").append(tableName.trim()).append("' ");
			cursor = mSQLiteDatabase.rawQuery(sql.toString(), null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return result;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()) {
			mSQLiteDatabase.close();
			mSQLiteDatabase = null;
		}
	}

}
