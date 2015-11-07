package com.duang.easyecard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	
	public static final String CREATE_BASICINFO = "create table BasicInfo ("
			+ "stu_id text primary key not null, "
			+ "password text not null, "
			+ "nick_name text, "
			+ "real_name text, "
			+ "gender integer, "
			+ "grade integer, "
			+ "college text, "
			+ "department text, "
			+ "email text, "
			+ "contact text, "
			+ "lost_flag integer, "
			+ "found_flag integer, "
			+ "found_id text)";
	
	private Context mContext;
	
	public MyDatabaseHelper(Context context, String name, CursorFactory
			factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)	{
		db.execSQL(CREATE_BASICINFO);
		Toast.makeText(mContext, "×¢²á³É¹¦", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)	{
		
	}
}
