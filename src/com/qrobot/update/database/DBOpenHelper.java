package com.qrobot.update.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���ݿ������
 * @author Administrator
 *
 */
public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "/sdcard/qrobot/update/record/update.db";
	private static final int VERSION = 1;
	
	/**
	 * ������
	 * @param context
	 */
	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS updatelog (id integer primary key autoincrement, " +
				"moduleID INTEGER, minVersion INTEGER, newVersion INTEGER," +
				" isTest BOOLEAN,releaseDate char(30), isRead BOOLEAN,versionDesc nvarchar(4000))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS updatelog");
		onCreate(db);
	}
}