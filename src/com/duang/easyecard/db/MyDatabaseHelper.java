package com.duang.easyecard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	
	public static final String CREATE_USERINFO = "create table UserInfo ("
			+ "stu_id char(11) primary key not null, "
			+ "password text not null, "
			+ "name text, "
			+ "real_name text, "
			+ "gender integer, "
			+ "contact text, "
			+ "grade integer, "
			+ "college text, "
			+ "department text, "
			+ "email text)";
	
	public static final String CREATE_LOSTEVENT = "create table LostEvent ("
			+ "event_id integer primary key autoincrement, "
			+ "add_date DATE, "
			+ "add_time TIME, "
			+ "owner_stu_id text, "
			+ "owner_name text, "
			+ "owner_contact text, "
			+ "founder_stu_id text, "
			+ "founder_name text, "
			+ "founder_contact text, "
			+ "lost_date DATE, "
			+ "lost_time TIME, "
			+ "lost_place text, "
			+ "description, "
			+ "duration text, "
			+ "due_date int, "
			+ "publiser_stu_id text, "
			+ "found_flag bool, "
			+ "close_flag bool)";
	
	public static final String CREATE_FOUNDEVENT = "create table FoundEvent ("
			+ "event_id integer primary key autoincrement, "
			+ "add_date DATE, "
			+ "add_time TIME, "
			+ "owner_stu_id text, "
			+ "owner_name text, "
			+ "owner_contact text, "
			+ "founder_stu_id text, "
			+ "founder_name text, "
			+ "founder_contact text, "
			+ "found_date DATE, "
			+ "found_time TIME, "
			+ "found_place text, "
			+ "duration text, "
			+ "due_date DATE, "
			+ "publiser_stu_id text, "
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
		db.execSQL(CREATE_USERINFO);
		db.execSQL(CREATE_LOSTEVENT);
		db.execSQL(CREATE_FOUNDEVENT);
		Toast.makeText(mContext, "已成功建立数据库", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)	{
		
	}
}
