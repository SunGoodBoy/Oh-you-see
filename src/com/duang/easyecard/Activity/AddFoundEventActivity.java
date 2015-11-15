package com.duang.easyecard.Activity;

import java.util.Calendar;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.User;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddFoundEventActivity extends BaseActivity	{

	
	private MyDatabaseHelper dbHelper;
	
	private EditText mStu_id;
	private EditText mName;
	private EditText mContact;
	private EditText mFound_place;
	private EditText mDescription;
	
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;
	private Button mSubmit_button;
	private Button mCancel_button;
	
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_found_event);
		
		mStu_id = (EditText) findViewById(R.id.add_found_event_stu_id_edit);
		mName = (EditText) findViewById(R.id.add_found_event_name_edit);
		mContact = (EditText) findViewById(R.id.add_found_event_contact_edit);
		mFound_place = (EditText) findViewById(R.id.add_event_found_place_edit);
		mDescription = (EditText) findViewById(R.id.add_event_found_decription_edit);
		
		mDatePicker = (DatePicker) findViewById(R.id.add_event_found_date_picker);
		mTimePicker = (TimePicker) findViewById(R.id.add_event_found_time_picker);
		
		mSubmit_button = (Button) findViewById(R.id.add_found_event_submit);
		mCancel_button = (Button) findViewById(R.id.add_found_event_cancel);
		
		mTimePicker.setIs24HourView(true);	//设置时钟为24小时制
		
		//打开或创建数据库
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		//提交按钮点击事件
		mSubmit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addFoundEvent_main(v);
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
	
	private void addFoundEvent_main(View v)	{
		
		String stu_id = mStu_id.getText().toString();
		String name = mName.getText().toString();
		String contact = mContact.getText().toString();
		
		//判断学号是否为空
		if (!stu_id.isEmpty())	{
			if (stu_id.length() == 11)	{
				if (!name.isEmpty())	{
					if (!contact.isEmpty())	{
						//将数据写入数据库
						writeDataToDb();
						finish();
						Toast.makeText(AddFoundEventActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(AddFoundEventActivity.this, "必须填写联系方式！", Toast.LENGTH_SHORT).show();
					}
				}
				else	{
					Toast.makeText(AddFoundEventActivity.this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
				}
				
			}
			else {
				Toast.makeText(AddFoundEventActivity.this, "学号必须为11位！", Toast.LENGTH_SHORT).show();
			}
		}
		else	{
			Toast.makeText(AddFoundEventActivity.this, "学号不能为空", Toast.LENGTH_LONG).show();
		}
	}

	//将数据写入数据库
	private void writeDataToDb() {
		
		//获得当前用户作为发布者
		String publisher = User.getCurrentUserStuId();
		
		String stu_id = mStu_id.getText().toString();
		String name = mName.getText().toString();
		String contact = mContact.getText().toString();
		String found_place = mFound_place.getText().toString();
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
		String found_date = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day_of_month);

		//获取TimePicker中的时间
		hour = mTimePicker.getCurrentHour();
		minute = mTimePicker.getCurrentMinute();
		String found_time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + "00";
		
		//写入数据库
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("insert into FoundEvent ("
				+ "publisher_stu_id, "
				+ "add_date, "
				+ "add_time, "
				+ "owner_stu_id, "
				+ "owner_name, "
				+ "owner_contact, "
				+ "found_date, "
				+ "found_time, "
				+ "found_place, "
				+ "description, "
				+ "duration, "
				+ "returned_flag, "
				+ "close_flag)"
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new String[] {publisher, add_date, add_time, stu_id, name, contact, found_date, found_time, 
						found_place, descrption, "30", "0", "0"});
		
	}
		
}

