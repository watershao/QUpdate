package com.qrobot.update.database;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VersionDB {
	private DBOpenHelper openHelper;

	public VersionDB(Context context) {
		openHelper = new DBOpenHelper(context);
	}
	
	/**
	 * ��ȡû�в��ŵİ汾��
	 * @param isRead
	 * @return
	 */
	public List<Integer> getData(boolean isRead){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select newVersion from updatelog where isRead=?", new String[]{String.valueOf(isRead)});
		
		List<Integer> newVersionList = new ArrayList<Integer>();
		
		while(cursor.moveToNext()){
			newVersionList.add(cursor.getInt(cursor.getColumnIndex("isRead")));
		}
		
		cursor.close();
		db.close();
		return newVersionList;
	}
	
	/**
	 * ����ÿ�θ��°汾�������Ϣ
	 * @param moduleID
	 * @param minVersion
	 * @param newVersion
	 * @param isTest
	 * @param releaseDate
	 * @param isRead
	 * @param versionDesc
	 */
	public void save(int moduleID,int minVersion,int newVersion,boolean isTest,
			String releaseDate,boolean isRead, String versionDesc){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		
		try{
			
			db.execSQL("insert into updatelog(moduleID, minVersion,newVersion,isTest," +
					" releaseDate,isRead,versionDesc) values(?,?,?,?,?,?,?)",
						new Object[]{moduleID,minVersion,newVersion,isTest,releaseDate,isRead,versionDesc});
			
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		db.close();
	}
	
	/**
	 * ����ָ���汾�Ĳ���״̬
	 * @param newVersion
	 */
	public void update(int newVersion){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		
		try{
			db.execSQL("update updatelog set isRead=? where newVersion=? ",
						new Object[]{newVersion});
			
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		db.close();
	}
	
	/**
	 * ɾ����Ӧ���¼�¼
	 * @param path
	 */
	public void delete(int newVersion){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("delete from updatelog where newVersion=?", new Object[]{newVersion});
		db.close();
	}
}
