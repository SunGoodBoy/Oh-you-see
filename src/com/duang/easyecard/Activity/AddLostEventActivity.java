package com.duang.easyecard.Activity;

import java.util.Calendar;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.User;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddLostEventActivity extends BaseActivity	{

	
	private MyDatabaseHelper dbHelper;
	
	private EditText mStu_id;
	private EditText mName;
	private EditText mContact;
	private EditText mLost_place;
	private EditText mDescription;
	
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;
	private Button mSubmit_button;
	private Button mCancel_button;
	
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_lost_event);
		
		mStu_id = (EditText) findViewById(R.id.add_lost_event_stu_id_edit);
		mName = (EditText) findViewById(R.id.add_lost_event_name_edit);
		mContact = (EditText) findViewById(R.id.add_lost_event_contact_edit);
		mLost_place = (EditText) findViewById(R.id.add_event_lost_place_edit);
		mDescription = (EditText) findViewById(R.id.add_event_lost_lost_decription_edit);
		
		mDatePicker = (DatePicker) findViewById(R.id.add_event_lost_date_picker);
		mTimePicker = (TimePicker) findViewById(R.id.add_event_lost_time_picker);
		
		mSubmit_button = (Button) findViewById(R.id.add_lost_event_submit);
		mCancel_button = (Button) findViewById(R.id.add_lost_event_cancel);
		
		mTimePicker.setIs24HourView(true);	//设置时钟为24小时制
		
		//打开或创建数据库
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		//提交按钮点击事件
		mSubmit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addLostEvent_main(v);
			}
		});
		

		//“取消”按钮的点击事件
		mCancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void addLostEvent_main(View v)	{
		
		String stu_id = mStu_id.getText().toString();
		String name = mName.getText().toString();
		String contact = mContact.getText().toString();
		
		//判断学号是否为空
		if (!stu_id.isEmpty())	{
			if (stu_id.length() == 11)	{
				if (!hasRepeatedEvent(stu_id)) {
					if (!name.isEmpty())	{
						if (!contact.isEmpty())	{
							//将数据写入数据库
							writeDataToDb();
							finish();
							Toast.makeText(AddLostEventActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
						}
						else {
							Toast.makeText(AddLostEventActivity.this, "必须填写联系方式！", Toast.LENGTH_SHORT).show();
						}
					}
					else	{
						Toast.makeText(AddLostEventActivity.this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Toast.makeText(AddLostEventActivity.this, "该学号已经发布了丢失信息！", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(AddLostEventActivity.this, "学号必须为11位！", Toast.LENGTH_SHORT).show();
			}
		}
		else	{
			Toast.makeText(AddLostEventActivity.this, "学号不能为空", Toast.LENGTH_LONG).show();
		}
	}

	// 该学号存在重复的未关闭的事件
	private boolean hasRepeatedEvent(String stu_id) {
		// 获得可读数据库
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		//遍历LostEvent表
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		if (cursor.moveToLast()) {
			do {
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))))
				{
					if (cursor.getString(cursor.getColumnIndex("close_flag")).equals("0")) {
						return true;
					}
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		return false;
	}
	
	// 将数据写入数据库
	private void writeDataToDb() {
		
		//获得当前用户作为发布者
		String publisher = User.getCurrentUserStuId();
		
		String stu_id = mStu_id.getText().toString();
		String name = mName.getText().toString();
		String contact = mContact.getText().toString();
		String lost_place = mLost_place.getText().toString();
		String descrption = mDescription.getText().toString();
		
		//将系统的当前日期传给add_date，当前时间传给add_time
		Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		String add_date = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day_of_month);
		String add_time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + "00";
		
		//获取DatePicker中的日期
		year = mDatePicker.getYear();
		month = mDatePicker.getMonth();
		day_of_month = mDatePicker.getDayOfMonth();
		String lost_date = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day_of_month);
		
		/** 打印时间以确认是否正确
		Log.d("year", String.valueOf(year));
		Log.d("month", String.valueOf(month));
		Log.d("day_of_month", String.valueOf(day_of_month));
		Log.d("hour", String.valueOf(hour));
		Log.d("minute", String.valueOf(minute));
		*/
		
		//获取TimePicker中的时间
		hour = mTimePicker.getCurrentHour();
		minute = mTimePicker.getCurrentMinute();
		String lost_time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + "00";
		
		//写入数据库
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("insert into LostEvent ("
				+ "publisher_stu_id, "
				+ "add_date, "
				+ "add_time, "
				+ "owner_stu_id, "
				+ "owner_name, "
				+ "owner_contact, "
				+ "lost_date, "
				+ "lost_time, "
				+ "lost_place, "
				+ "description, "
				+ "duration, "
				+ "found_flag, "
				+ "close_flag)"
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new String[] {publisher, add_date, add_time, stu_id, name, contact, lost_date, lost_time, 
						lost_place, descrption, "30", "0", "0"});
	}
	
}

