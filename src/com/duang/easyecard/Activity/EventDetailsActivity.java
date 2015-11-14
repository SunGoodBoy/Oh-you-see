package com.duang.easyecard.Activity;

import java.util.StringTokenizer;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.Event;
import com.duang.easyecard.model.User;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDetailsActivity extends BaseActivity{
	
	private MyDatabaseHelper dbHelper;
	
	//private ImageView imgvUserImg;
	
	private TextView tvStu_id;
	private TextView tvName;
	private TextView tvContact;
	private TextView tvEventTimeTitle;
	private TextView tvEventTime;
	private TextView tvEventPlaceTitle;
	private TextView tvEventPlace;
	private TextView tvEventDescription;
	private TextView tvEventPublisher;
	private TextView tvEventAddTime;
	private TextView tvEventState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);
		
		//控件的初始化 要在加载布局文件之后
		//imgvUserImg = (ImageView) findViewById(R.id.event_details_img);
		
		tvStu_id = (TextView) findViewById(R.id.event_details_stu_id);
		tvName = (TextView) findViewById(R.id.event_details_name);
		tvContact = (TextView) findViewById(R.id.event_details_contact);
		tvEventTimeTitle = (TextView) findViewById(R.id.event_details_event_time_title);
		tvEventTime = (TextView) findViewById(R.id.event_details_event_time);
		tvEventPlaceTitle = (TextView) findViewById(R.id.event_details_event_place_title);
		tvEventPlace = (TextView) findViewById(R.id.event_details_event_place);
		tvEventDescription = (TextView) findViewById(R.id.event_details_event_description);
		tvEventPublisher = (TextView) findViewById(R.id.event_details_event_publisher);
		tvEventAddTime = (TextView) findViewById(R.id.event_details_event_add_time);
		tvEventState = (TextView) findViewById(R.id.event_details_event_state);
		
		//打开或创建数据库
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		Intent intent = getIntent();
		String data = intent.getStringExtra("extra_data");
		Log.d("extra_data in EventDetailsActivity", data);
		
		String split = "_";
		StringTokenizer token = new StringTokenizer(data, split);
		String stu_id = token.nextToken();
		//Log.d("EventDetailsActivity stu_id in data", stu_id);
		
		int FLAG = Integer.parseInt(token.nextToken());
		//Log.d("EventDetailsActivity FLAG in data", String.valueOf(FLAG));
		
		tvStu_id.setText(stu_id);	//显示学号
		
		//判断FLAG的值，选择不同的表进行查询
		switch (FLAG)
		{
			case 3:
			case 1:
				getInfoFromLostEvent(stu_id);
				break;
			case 2:
				getInfoFromFoundEvent(stu_id);
				break;
			default:
		}
		
	}

	private void getInfoFromLostEvent(String stu_id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))))
				{
					//失主姓名
					tvName.setText(cursor.getString(cursor.getColumnIndex("owner_name")));
					//失主联系方式
					tvContact.setText(cursor.getString(cursor.getColumnIndex("owner_contact")));
					//显示丢失时间，格式为“2015年11月14日22点54分”
					tvEventTimeTitle.setText("丢失时间");
					String date = cursor.getString(cursor.getColumnIndex("lost_date"));
					String split = "_";
					StringTokenizer token = new StringTokenizer(date, split);
					String year = token.nextToken();
					String month = token.nextToken();
					String day = token.nextToken();
					String time_in_day = cursor.getString(cursor.getColumnIndex("lost_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					String hour = token.nextToken();
					String minute = token.nextToken();
					tvEventTime.setText(year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					Log.d("EventTime", year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					
					//显示丢失地点
					tvEventPlaceTitle.setText("丢失地点");
					tvEventPlace.setText(cursor.getString(cursor.getColumnIndex("lost_place")));
					//显示描述
					tvEventDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
					//显示发布者（仅学号）
					tvEventPublisher.setText(cursor.getString(cursor.getColumnIndex("publiser_stu_id")));
					//显示发布时间
					date = cursor.getString(cursor.getColumnIndex("lost_date"));
					split = "_";
					token = new StringTokenizer(date, split);
					year = token.nextToken();
					month = token.nextToken();
					day = token.nextToken();
					time_in_day = cursor.getString(cursor.getColumnIndex("lost_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					hour = token.nextToken();
					minute = token.nextToken();
					tvEventAddTime.setText(year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					Log.d("EventTime", year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					
					//显示事件状态  {"已经找到", "正在寻找"}
					if (cursor.getColumnIndex("found_flag") == 0)
					{
						tvEventState.setText("正在寻找");
					}
					else
					{
						tvEventState.setText("已经找到");
					}
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
	}
	
	private void getInfoFromFoundEvent(String stu_id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("FoundEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))))
				{
					//失主姓名
					tvName.setText(cursor.getString(cursor.getColumnIndex("owner_name")));
					//拾获者联系方式
					tvContact.setText(cursor.getString(cursor.getColumnIndex("owner_contact")));
					//显示拾获时间，格式为“2015年11月14日22点54分”
					tvEventTimeTitle.setText("拾获时间");
					String date = cursor.getString(cursor.getColumnIndex("found_date"));
					String split = "_";
					StringTokenizer token = new StringTokenizer(date, split);
					String year = token.nextToken();
					String month = token.nextToken();
					String day = token.nextToken();
					String time_in_day = cursor.getString(cursor.getColumnIndex("found_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					String hour = token.nextToken();
					String minute = token.nextToken();
					tvEventTime.setText(year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					Log.d("EventTime", year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					
					//显示拾获地点
					tvEventPlaceTitle.setText("拾获地点");
					tvEventPlace.setText(cursor.getString(cursor.getColumnIndex("found_place")));
					//显示描述
					tvEventDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
					//显示发布者（仅学号）
					tvEventPublisher.setText(cursor.getString(cursor.getColumnIndex("publiser_stu_id")));
					//显示发布时间
					date = cursor.getString(cursor.getColumnIndex("lost_date"));
					split = "_";
					token = new StringTokenizer(date, split);
					year = token.nextToken();
					month = token.nextToken();
					day = token.nextToken();
					time_in_day = cursor.getString(cursor.getColumnIndex("lost_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					hour = token.nextToken();
					minute = token.nextToken();
					tvEventAddTime.setText(year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					Log.d("EventTime", year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					
					//显示事件状态  {"已经归还", "寻找失主"}
					if (cursor.getColumnIndex("returned_flag") == 0)
					{
						tvEventState.setText("寻找失主");
					}
					else
					{
						tvEventState.setText("已经归还");
					}
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
	}

	
}
