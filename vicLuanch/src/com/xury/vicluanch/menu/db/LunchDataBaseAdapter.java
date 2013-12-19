package com.xury.vicluanch.menu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * com.Aina.Android Pro_SQLiteDatabase
 * 
 * @author
 * @version
 */
public class LunchDataBaseAdapter {

	private Context mContext = null;
	private SQLiteDatabase mSQLiteDatabase = null;
	private DatabaseHelper dh = null;
	/**
	 * 锟斤拷锟斤拷锟揭伙拷锟斤拷锟绞�
	 */
	public static final int C_iDBFirstHint = 1;
	/**
	 * 锟斤拷锟斤拷诙锟斤拷锟斤拷锟绞�
	 */
	public static final int C_iDBSecondHint = 2;
	private static final String DB_NAME = "lunchm.db";
	private static final int DB_VERSION = 1;
	private static final String TABLE_NAME = "lundatem";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USER = "user";
	public static final String COLUMN_Date = "lundate";
	public static final String Expanding1 = "expanding1"; // 锟斤拷展锟街讹拷1
	public static final String Expanding2 = "expanding2"; // 锟斤拷展锟街讹拷2
	public static final String Expanding3 = "expanding3"; // 锟斤拷展锟街讹拷3
	public static final String Expanding4 = "expanding4"; // 锟斤拷展锟街讹拷4
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ COLUMN_USER + " varchar," + COLUMN_Date + " varchar,"
			+ Expanding1 + " varchar," + Expanding2 + " varchar," + Expanding3
			+ " varchar," + Expanding4 + " varchar);";

	public LunchDataBaseAdapter(Context context) {
		mContext = context;
	}

	/**
	 * 锟斤拷锟斤拷菘锟�
	 */
	public void open() {
		dh = new DatabaseHelper(mContext, DB_NAME, null, DB_VERSION,
				CREATE_TABLE, TABLE_NAME);
		mSQLiteDatabase = dh.getWritableDatabase();
	}

	/**
	 * 锟截憋拷锟斤拷菘锟�
	 */
	public void close() {
		dh.close();
	}

	/**
	 * 锟斤拷锟斤拷
	 */
	public void insert(long userID, String date,long companyID) {
		try {
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_USER, userID);
			cv.put(COLUMN_Date, date);
			cv.put(Expanding1, companyID);
			mSQLiteDatabase.insert(TABLE_NAME, null, cv);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 锟睫革拷
	 */
	public void update(String date, long id,long compantID) {
		try {
			ContentValues cv = new ContentValues();  
			cv.put(COLUMN_Date, date);
			mSQLiteDatabase.update(TABLE_NAME, cv, "user="
					+ id+" and "+"expanding1="+compantID, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 删锟斤拷
	 */
	public void delete(long id) {
		try {
			mSQLiteDatabase.delete(TABLE_NAME, "user=" + id, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 锟斤拷询
	 */
	public Cursor select(long id,long companyID) {
		Cursor cursor = null;
		try {
			String sql = "SELECT * FROM " + TABLE_NAME + " where user=" + id+" and "+"expanding1="+companyID;
			cursor = mSQLiteDatabase.rawQuery(sql, null);
			return cursor;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}