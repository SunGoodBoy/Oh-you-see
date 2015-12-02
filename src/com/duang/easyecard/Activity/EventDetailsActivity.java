package com.duang.easyecard.Activity;

import java.lang.reflect.Field;
import java.util.StringTokenizer;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailsActivity extends BaseActivity implements OnClickListener{
	
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
	
	private Button btnCloseEvent;
	private Button btnFunction;
	
	private int FLAG;   //FLAG用于标识事件类型  {LostEvent为1, FoundEvent为2}
	private String mPublisher; //发布者
	private String mCloseFlag; //事件的关闭标志
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setOverflowButtonAlways();
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
		
		btnCloseEvent = (Button) findViewById(R.id.event_details_close_event_button);
		btnFunction = (Button) findViewById(R.id.event_details_function_button);
		
		//打开或创建数据库
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		if (dbHelper == null)
		{
			Log.e("dbHelper", "Fail to open database.");
		}		
		
		initViews();
		
	}

	private void initViews() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String data = intent.getStringExtra("extra_data");
		Log.d("extra_data in EventDetailsActivity", data);
		
		String split = "_";
		StringTokenizer token = new StringTokenizer(data, split);
		String stu_id = token.nextToken();
		//Log.d("EventDetailsActivity stu_id in data", stu_id);
		
		FLAG = Integer.parseInt(token.nextToken());
		Log.d("EventDetailsActivity FLAG in data", String.valueOf(FLAG));
		
		tvStu_id.setText(stu_id);	//显示学号
		
		//判断FLAG的值，选择不同的表进行查询
		switch (FLAG)
		{
			case 3:
			case 1:
				getInfoFromLostEvent(stu_id);
				//将btnFunction显示为“我找到了”
				btnFunction.setText("我找到了");
				//如果当前用户是事件发布者，并且事件未被关闭，将“关闭事件”按钮置为可点击
				if (User.getCurrentUserStuId().equals(mPublisher) &&
						mCloseFlag.equals("0")) {
					btnCloseEvent.setClickable(true);
					btnCloseEvent.setOnClickListener(this);
				}
				break;
			case 2:
				getInfoFromFoundEvent(stu_id);
				//将btnFunction显示为“我是失主”
				btnFunction.setText("我是失主");
				//如果当前用户是事件发布者，并且事件未被关闭，将“关闭事件”按钮置为可点击
				if (User.getCurrentUserStuId().equals(mPublisher) &&
						mCloseFlag.equals("0")) {
					btnCloseEvent.setClickable(true);
					btnCloseEvent.setOnClickListener(this);
				}
				break;
			default:
		}
		
		btnFunction.setOnClickListener(this);
	}

	//从LostEvent获取信息，填充到布局
	protected void getInfoFromLostEvent(String stu_id) {
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
					String split = "-";
					StringTokenizer token = new StringTokenizer(date, split);
					String year = null, month = null, day = null;
					if (token.hasMoreTokens())
					{
						year = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						month = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						day = token.nextToken();
					}
					String time_in_day = cursor.getString(cursor.getColumnIndex("lost_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					String hour = null, minute = null;
					if (token.hasMoreTokens())
					{
						hour = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						minute = token.nextToken();
					}
					tvEventTime.setText(year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					Log.d("EventLostTime", year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					//显示丢失地点
					tvEventPlaceTitle.setText("丢失地点");
					tvEventPlace.setText(cursor.getString(cursor.getColumnIndex("lost_place")));
					//显示描述
					tvEventDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
					//显示发布者（仅学号）
					tvEventPublisher.setText(cursor.getString(cursor.getColumnIndex("publisher_stu_id")));
					//将发布者赋给mPublisher
					mPublisher = cursor.getString(cursor.getColumnIndex("publisher_stu_id"));
					//显示发布时间
					date = cursor.getString(cursor.getColumnIndex("lost_date"));
					split = "-";
					token = new StringTokenizer(date, split);
					year = null;
					month = null;
					day = null;
					if (token.hasMoreTokens())
					{
						year = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						month = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						day = token.nextToken();
					}
					time_in_day = cursor.getString(cursor.getColumnIndex("lost_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					hour = null;
					minute = null;
					if (token.hasMoreTokens())
					{
						hour = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						minute = token.nextToken();
					}
					tvEventAddTime.setText(year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					Log.d("EventAddTime", year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					
					//把事件是否关闭的状态传递给mCloseFlag
					mCloseFlag = cursor.getString(cursor.getColumnIndex("close_flag"));
					Log.d("close_flag", cursor.getString(cursor.getColumnIndex("close_flag")));
					
					//显示事件状态  {"已经找到", "正在寻找", "已经关闭"}
					if (cursor.getString(cursor.getColumnIndex("close_flag")).equals("0")) {
						if (cursor.getString(cursor.getColumnIndex("found_flag")).equals("0"))
						{
							Log.d("found_flag", cursor.getString(cursor.getColumnIndex("found_flag")));
							tvEventState.setText("正在寻找");
						}
						else
						{
							Log.d("found_flag", cursor.getString(cursor.getColumnIndex("found_flag")));
							tvEventState.setText("已经找到");
						}
					} else {
						tvEventState.setText("已经关闭");
					}
					
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
	}
	
	//从FoundEvent获取信息，填充到布局
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
					String split = "-";
					StringTokenizer token = new StringTokenizer(date, split);
					String year = null, month = null, day = null;
					if (token.hasMoreTokens())
					{
						year = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						month = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						day = token.nextToken();
					}

					String time_in_day = cursor.getString(cursor.getColumnIndex("found_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					String hour = null;
					String minute = null;
					if (token.hasMoreTokens())
					{
						hour = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						minute = token.nextToken();
					}
					tvEventTime.setText(year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					Log.d("EventFoundTime", year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					
					//显示拾获地点
					tvEventPlaceTitle.setText("拾获地点");
					tvEventPlace.setText(cursor.getString(cursor.getColumnIndex("found_place")));
					//显示描述
					tvEventDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
					//显示发布者（仅学号）
					tvEventPublisher.setText(cursor.getString(cursor.getColumnIndex("publisher_stu_id")));
					//将发布者赋给mPublisher
					mPublisher = cursor.getString(cursor.getColumnIndex("publisher_stu_id"));
					//显示发布时间
					date = cursor.getString(cursor.getColumnIndex("add_date"));
					split = "-";
					token = new StringTokenizer(date, split);
					if (token.hasMoreTokens())
					{
						year = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						month = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						day = token.nextToken();
					}
					time_in_day = cursor.getString(cursor.getColumnIndex("add_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					hour = null;
					minute = null;
					if (token.hasMoreTokens())
					{
						hour = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						minute = token.nextToken();
					}
					tvEventAddTime.setText(year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					Log.d("EventAddTime", year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分");
					
					//把事件是否关闭的状态传递给mCloseFlag
					mCloseFlag = cursor.getString(cursor.getColumnIndex("close_flag"));
					Log.d("close_flag", cursor.getString(cursor.getColumnIndex("close_flag")));
					
					//显示事件状态  {"已经归还", "寻找失主", "已经关闭"}
					if (cursor.getString(cursor.getColumnIndex("close_flag")).equals("0")) {
						if (cursor.getString(cursor.getColumnIndex("returned_flag")).equals("0"))
						{
							Log.d("returned_flag", cursor.getString(cursor.getColumnIndex("returned_flag")));
							tvEventState.setText("寻找失主");
						}
						else
						{
							Log.d("returned_flag", cursor.getString(cursor.getColumnIndex("returned_flag")));
							tvEventState.setText("已经归还");
						}
					} else {
						tvEventState.setText("已经关闭");
					}
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
	}
	
	// 关闭LostEvent
	private void closeLostEvent() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("update LostEvent set close_flag = ? where publisher_stu_id = ?",
				new String[] { "1", mPublisher});
		db.close();
	}
	// 关闭FoundEvent
	private void closeFoundEvent() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("update FoundEvent set close_flag = ? where publisher_stu_id = ?",
				new String[] { "1", mPublisher});
		db.close();
	}
	//监听Button的点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		// 监听“关闭事件”按钮
		case R.id.event_details_close_event_button:
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("提示");
			dialog.setMessage("确定关闭该事件吗？");
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 根据事件类型，将改动写入数据库
					if (FLAG == 1) {
						closeLostEvent();
						mCloseFlag = "1";
					} else {
						closeFoundEvent();
						mCloseFlag = "1";
					}
				}
			});
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			dialog.show();
			break;
		
		case R.id.event_details_function_button:
			Toast.makeText(this, "那就尽快取得联系吧！^_^nn-", Toast.LENGTH_SHORT);
			break;
			default:
		}
	}
	
	// 创建菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();  
        inflater.inflate(R.menu.menu_event_details, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	// 设置ActionBar的按钮在标题栏一直显示
	private void setOverflowButtonAlways()
	{
		try
		{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	// 设置菜单按钮的点击事件
	public boolean onMenuItemSelected(int featureId, MenuItem item)	{
		switch (item.getItemId())	{
		case R.id.action_alter_event:
			if (mPublisher.equals(User.getCurrentUserStuId())) {
				if (mCloseFlag.equals("0")) {
					Intent intent = new Intent(EventDetailsActivity.this, AlterEventActivity.class);
					intent.putExtra("stu_id__FLAG", tvStu_id.getText().toString() + "__" + FLAG);
					startActivity(intent);
					finish();  // 销毁本活动，以方便重新加载
				} else {
					Toast.makeText(this, "无法修改已关闭事件", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "只能修改您自己发布的事件", Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}
	
}
