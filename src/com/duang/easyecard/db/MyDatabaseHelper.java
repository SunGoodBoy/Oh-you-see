package com.duang.easyecard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	
	public static final String CREATE_BASIC_INFO = "create table Book ("
			+ "stu_id char(11) primary key not null, "
			+ "nick_name text, "
			+ "real_name text, "
			+ "gender integer, "
			+ "college text, "
			+ "department text, "
			+ "contact text, "
			+ "lost_flag integer not null, "
			+ "found_flag integer not null)";
	
	private Context mContext;
	
	public MyDatabaseHelper(Context context, String name, CursorFactory
			factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)	{
		db.execSQL(CREATE_BASIC_INFO);
		Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)	{
		
	}
}
