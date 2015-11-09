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
	
	public static final String CREATE_LOSTEVENT = "create table LostEvent ("
			+ "event_id integer primary key autoincrement, "
			+ "owner_stu_id text, "
			+ "owner_name text, "
			+ "owner_contact text, "
			+ "founder_stu_id text, "
			+ "founder_name text, "
			+ "founder_contact text, "
			+ "lost_date DATE, "
			+ "lost_place text, "
			+ "duration text, "
			+ "due_date DATE, "
			+ "found_flag bool, "
			+ "close_flag bool)";
	
	public static final String CREATE_FOUNDEVENT = "create table FoundEvent ("
			+ "event_id integer primary key autoincrement, "
			+ "owner_stu_id text, "
			+ "owner_name text, "
			+ "owner_contact text, "
			+ "founder_stu_id text, "
			+ "founder_name text, "
			+ "founder_contact text, "
			+ "found_date DATE, "
			+ "found_place text, "
			+ "duration text, "
			+ "due_date DATE, "
			+ "returned_flag bool, "
			+ "close_flag bool)";
			
			
	private Context mContext;
	
	public MyDatabaseHelper(Context context, String name, CursorFactory
			factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)	{
		db.execSQL(CREATE_BASICINFO);
		db.execSQL(CREATE_LOSTEVENT);
		db.execSQL(CREATE_FOUNDEVENT);
		Toast.makeText(mContext, "已成功建立数据库", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)	{
		
	}
}
