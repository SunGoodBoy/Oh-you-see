package com.duang.ohyousee.Activity;

import java.util.Calendar;
import java.util.StringTokenizer;

import com.duang.easyecard.R;
import com.duang.ohyousee.db.MyDatabaseHelper;
import com.duang.ohyousee.model.User;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
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
	
	private int FLAG;  // 用于标识事件的类型，LostEvent为1，FoundEvent为2
	private String mOldStuId;  // OldStuId保存原学号
	private String mPublisher = User.getCurrentUserStuId();  // 发布者
	
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
		mOldStuId = stu_id;
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
				Intent intent = new Intent(AlterEventActivity.this, EventDetailsActivity.class);
				intent.putExtra("extra_data", mStuId.getText().toString() + "__" + FLAG);
				startActivity(intent);
				finish();
			}
		});
		//“取消”按钮的点击事件
		mCancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
					Log.d("year-month-day", year + "-" + month + "-" + day);
					// 设置DatePicker默认日期
					mDatePicker.updateDate(year, month - 1, day);
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
					Log.d("hour:minute", hour + ":" + minute);
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
					Log.d("year-month-day", year + "-" + month + "-" + day);
					// 设置DatePicker默认日期
					mDatePicker.updateDate(year, month - 1, day);
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
					Log.d("hour:minute", hour + ":" + minute);
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
		String stu_id = mStuId.getText().toString();
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
						Toast.makeText(AlterEventActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(AlterEventActivity.this, "必须填写联系方式！", Toast.LENGTH_SHORT).show();
					}
				}
				else	{
					Toast.makeText(AlterEventActivity.this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(AlterEventActivity.this, "学号必须为11位！", Toast.LENGTH_SHORT).show();
			}
		}
		else	{
			Toast.makeText(AlterEventActivity.this, "学号不能为空", Toast.LENGTH_LONG).show();
		}
		
	}

	// 将数据写入数据库
	private void writeDataToDb() {
		// 根据FLAG的值确定EVENT类型
		String EVENT = null;
		String EVENT_DATE = null;
		String EVENT_TIME = null;
		String EVENT_PLACE = null;
		if (FLAG == 1) {
			EVENT = "LostEvent";
			EVENT_DATE = "lost_date";
			EVENT_TIME = "lost_time";
			EVENT_PLACE = "lost_place";
		} else if (FLAG == 2) {
			EVENT = "FoundEvent";
			EVENT_DATE = "found_date";
			EVENT_TIME = "found_time";
			EVENT_PLACE = "found_place";
		} else {
			Log.e("Event Flag", "Unexpected FLAG");
		}
		String stu_id = mStuId.getText().toString();
		String name = mName.getText().toString();
		String contact = mContact.getText().toString();
		String event_place = mEventPlace.getText().toString();
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
		String event_date = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day_of_month);
		//获取TimePicker中的时间
		hour = mTimePicker.getCurrentHour();
		minute = mTimePicker.getCurrentMinute();
		String event_time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + "00";
		
		// 获取可写数据库
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// 判断学号是否改变
		if (stu_id.equals(mOldStuId)) {
			// 学号未改变，更新已有事件
			db.execSQL("update " + EVENT +" set owner_name = ? where owner_stu_id = ?",
							new String[]{name, stu_id});
			db.execSQL("update " + EVENT +" set owner_contact = ? where owner_stu_id = ?",
					new String[]{contact, stu_id});
			db.execSQL("update " + EVENT +" set " + EVENT_DATE+ "= ? where owner_stu_id = ?",
					new String[]{event_date, stu_id});
			db.execSQL("update " + EVENT +" set " + EVENT_TIME + "= ? where owner_stu_id = ?",
					new String[]{event_time, stu_id});
			db.execSQL("update " + EVENT +" set " + EVENT_PLACE + "= ? where owner_stu_id = ?",
					new String[]{event_place, stu_id});
			db.execSQL("update " + EVENT +" set description = ? where owner_stu_id = ?",
					new String[]{descrption, stu_id});
		} else {
			// 学号改变，新建事件
			db.execSQL("insert into " + EVENT + " ("
					+ "add_date, "
					+ "add_time, "
					+ "owner_stu_id, "
					+ "owner_name, "
					+ "owner_contact, "
					+ EVENT_DATE + ", "
					+ EVENT_TIME + ", "
					+ EVENT_PLACE + ", "
					+ "description, "
					+ "duration, "
					+ "publisher_stu_id, "
					+ "found_flag, "
					+ "close_flag)"
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					new String[] {add_date, add_time, stu_id, name, contact, event_date, event_time, 
							event_place, descrption, "30", mPublisher, "0", "0"});
			// 删除旧学号的事件
			db.execSQL("delete from " + EVENT +" where owner_stu_id = ?",
					new String[] {mOldStuId});
		}
	}
	
}
