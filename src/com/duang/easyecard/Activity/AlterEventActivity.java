package com.duang.easyecard.Activity;

import java.util.Calendar;
import java.util.StringTokenizer;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.User;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlterEventActivity extends BaseActivity {

	private MyDatabaseHelper dbHelper;
	
	private EditText mStuId;
	private EditText mName;
	private EditText mContact;
	private EditText mEventPlace;
	private EditText mDescription;
	
	private TextView mEventPlaceTitle;
	private TextView mEventDateTitle;
	private TextView mEventTimeTitle;
	
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;
	private Button mSubmit_button;
	private Button mCancel_button;
	
	private int FLAG; // 用于标识事件的类型，LostEvent为1，FoundEvent为2
	
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_lost_event);
		
		mStuId = (EditText) findViewById(R.id.add_lost_event_stu_id_edit);
		mName = (EditText) findViewById(R.id.add_lost_event_name_edit);
		mContact = (EditText) findViewById(R.id.add_lost_event_contact_edit);
		mEventPlace = (EditText) findViewById(R.id.add_event_lost_place_edit);
		mDescription = (EditText) findViewById(R.id.add_event_lost_lost_decription_edit);
		
		mEventPlaceTitle = (TextView) findViewById(R.id.add_event_lost_place);
		mEventDateTitle = (TextView) findViewById(R.id.add_event_lost_date_text);
		mEventTimeTitle = (TextView) findViewById(R.id.add_event_lost_time_text);
		
		mDatePicker = (DatePicker) findViewById(R.id.add_event_lost_date_picker);
		mTimePicker = (TimePicker) findViewById(R.id.add_event_lost_time_picker);
		
		mSubmit_button = (Button) findViewById(R.id.add_lost_event_submit);
		mCancel_button = (Button) findViewById(R.id.add_lost_event_cancel);
		
		mTimePicker.setIs24HourView(true);	//设置时钟为24小时制
		mSubmit_button.setText("保存修改");
		
		//打开或创建数据库
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		initViews();
	}
	
	private void initViews() {
		
		Intent intent = getIntent();
		String data = intent.getStringExtra("stu_id__FLAG");

		// 获取事件失主的学号
		String split = "_";
		StringTokenizer token = new StringTokenizer(data, split);
		String stu_id = token.nextToken();
		Log.d("AlterEventActivity stu_id in data", stu_id);
		// 获取事件的类型
		FLAG = Integer.parseInt(token.nextToken());
		Log.d("AlterEventActivity FLAG in data", String.valueOf(FLAG));
		
		// 显示学号
		mStuId.setText(stu_id);
		
		switch (FLAG) {
			case 1:
				getInfoFromLostEvent(stu_id);
				break;
			case 2:
				getInfoFromFoundEvent(stu_id);
				break;
			default:
				break;
		}
		
		
		
		//提交按钮点击事件
		mSubmit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alterEventMain(v);
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				Toast.makeText(AlterEventActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		//“取消”按钮的点击事件
		mCancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});
	}
	
	private void getInfoFromFoundEvent(String stu_id) {
		// 从FoundEvent中搜索信息
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("FoundEvent", null, null, null, null, null, null);
		
		if (cursor.moveToLast())
		{
			do{
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))) ||
						cursor.getString(cursor.getColumnIndex("close_flag")).equals("0"))
				{
					// 失主姓名
					mName.setText(cursor.getString(cursor.getColumnIndex("owner_name")));
					// 失主联系方式
					mContact.setText(cursor.getString(cursor.getColumnIndex("owner_contact")));
					// 显示为“设置拾获日期”
					mEventDateTitle.setText("设置拾获日期");
					// 获取拾获日期
					String date = cursor.getString(cursor.getColumnIndex("found_date"));
					String split = "-";
					StringTokenizer token = new StringTokenizer(date, split);
					int year = 0, month = 0, day = 0;
					if (token.hasMoreTokens())
					{
						year = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						month = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						day = Integer.parseInt(token.nextToken());
					}
					// 初始化DatePicker
					mDatePicker.init(year, month, day, new OnDateChangedListener() {
						@Override
						public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
							// TODO Auto-generated method stub
						}
					});
					// 显示为“设置拾获时间”
					mEventTimeTitle.setText("设置拾获时间");
					// 获取丢失时间
					String time_in_day = cursor.getString(cursor.getColumnIndex("found_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					int hour = 0, minute = 0;
					if (token.hasMoreTokens())
					{
						hour = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						minute = Integer.parseInt(token.nextToken());
					}
					// 初始化TimePicker
					mTimePicker.setCurrentHour(hour);
					mTimePicker.setCurrentMinute(minute);
					//显示为“丢失地点”
					mEventPlaceTitle.setText("拾获地点");
					mEventPlace.setText(cursor.getString(cursor.getColumnIndex("found_place")));
					//显示描述
					mDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
	}

	private void getInfoFromLostEvent(String stu_id) {
		// 从LostEvent中搜索信息
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		
		if (cursor.moveToLast())
		{
			do{
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))) ||
						cursor.getString(cursor.getColumnIndex("close_flag")).equals("0"))
				{
					// 失主姓名
					mName.setText(cursor.getString(cursor.getColumnIndex("owner_name")));
					// 失主联系方式
					mContact.setText(cursor.getString(cursor.getColumnIndex("owner_contact")));
					// 显示为“设置丢失日期”
					mEventDateTitle.setText("设置丢失日期");
					// 获取丢失日期
					String date = cursor.getString(cursor.getColumnIndex("lost_date"));
					String split = "-";
					StringTokenizer token = new StringTokenizer(date, split);
					int year = 0, month = 0, day = 0;
					if (token.hasMoreTokens())
					{
						year = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						month = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						day = Integer.parseInt(token.nextToken());
					}
					// 初始化DatePicker
					mDatePicker.init(year, month, day, new OnDateChangedListener() {
						@Override
						public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
							// TODO Auto-generated method stub
						}
					});
					// 显示为“设置丢失时间”
					mEventTimeTitle.setText("设置丢失时间");
					// 获取丢失时间
					String time_in_day = cursor.getString(cursor.getColumnIndex("lost_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					int hour = 0, minute = 0;
					if (token.hasMoreTokens())
					{
						hour = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						minute = Integer.parseInt(token.nextToken());
					}
					// 初始化TimePicker
					mTimePicker.setCurrentHour(hour);
					mTimePicker.setCurrentMinute(minute);
					//显示为“丢失地点”
					mEventPlaceTitle.setText("丢失地点");
					mEventPlace.setText(cursor.getString(cursor.getColumnIndex("lost_place")));
					//显示描述
					mDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
	}

	private void alterEventMain(View v)	{
		
		
	}

	// 将数据写入数据库
	private void writeDataToDb() {
		
		
	}
	
	// 监听Back按钮的点击
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)	{
			// 不考虑信息是否修改，直接销毁活动
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		}
		return false;
	}
}
