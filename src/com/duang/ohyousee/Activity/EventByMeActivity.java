package com.duang.ohyousee.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.ohyousee.R;
import com.duang.ohyousee.db.MyDatabaseHelper;
import com.duang.ohyousee.model.Event;
import com.duang.ohyousee.model.User;
import com.duang.ohyousee.util.EventAdapter;
import com.duang.ohyousee.util.XListView;
import com.duang.ohyousee.util.XListView.IXListViewListener;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EventByMeActivity extends BaseActivity implements IXListViewListener, OnItemClickListener
{
	private MyDatabaseHelper dbHelper;
	
	private XListView xListView;
	private EventAdapter mAdapter;
	private List<Event> eventList = new ArrayList<Event>();
	private Handler mHandler;
	
	private CheckBox lostEventCheckBox;
	private CheckBox foundEventCheckBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_by_me);
		Log.d("EventByMeActivity", "onCreate is called");
		
		//打开或创建数据库
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		//初始化控件
		lostEventCheckBox = (CheckBox) findViewById(R.id.event_by_me_check_box_lost);
		foundEventCheckBox = (CheckBox) findViewById(R.id.event_by_me_check_box_found);
		
		//初始化视图，显示全部事件
		doSearchQueryBoth();
		initView();
		
		//对CheckBox设置监听事件
    	lostEventCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// lostEventCheckBox被选中
					if (foundEventCheckBox.isChecked())
					{
						doSearchQueryBoth();
						initView();
					} else {
						doSearchQueryInLostEvent();
						initView();
					}
				} else {
					// lostEventCheckBox没有被选中的情况
					if (foundEventCheckBox.isChecked())
					{
						doSearchQueryInFoundEvent();
						initView();
					} else {
						// 都没有被选中
						dontSearchQuery();
						initView();
					}
				}
			}
		});
    	
    	foundEventCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (lostEventCheckBox.isChecked()) {
						doSearchQueryBoth();
						initView();
					} else {
						doSearchQueryInFoundEvent();
						initView();
					}
				} else {
					// foundEventCheckBox没有被选中的情况
					if (lostEventCheckBox.isChecked())
					{
						doSearchQueryInLostEvent();
						initView();
					} else {
						// 都没有被选中
						dontSearchQuery();
						initView();
					}
				}
			}
		});
    	
    	
	}

	private void initView() {
		xListView = (XListView) findViewById(R.id.xListView_event_by_me);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
		xListView.setAdapter(mAdapter);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);
		mHandler = new Handler();
	}
	
	//不进行搜索，建立空的eventList
	private void dontSearchQuery() {
		eventList = new ArrayList<Event>();
		mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
	}

	/*
	 * 在LostEvent表中搜索
	 * 只搜索owner_stu_id和owner_name列
	 */
	private void doSearchQueryInLostEvent() {
		
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	//遍历LostEvent表
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if ((cursor.getString(cursor.getColumnIndex("publisher_stu_id")))
						.equals(User.getCurrentUserStuId()))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
    				//Log.d("SearchResult eventListSize", String.valueOf(eventList.size()));
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
	}

	/*
	 * 在FoundEvent表中搜索
	 * 只搜索owner_stu_id和owner_name列
	 */
	private void doSearchQueryInFoundEvent() {
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	//遍历LostEvent表
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if ((cursor.getString(cursor.getColumnIndex("publisher_stu_id")))
						.equals(User.getCurrentUserStuId()))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
    				//Log.d("SearchResult eventListSize", String.valueOf(eventList.size()));
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
	}
	
	/*
	 * 既在LostEvent表中搜素，也在FoundEvent表中搜索
	 * 只搜索owner_stu_id和owner_name列
	 */
	private void doSearchQueryBoth() {
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	//遍历LostEvent表
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if ((cursor.getString(cursor.getColumnIndex("publisher_stu_id")))
						.equals(User.getCurrentUserStuId()))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
    				//Log.d("SearchResult eventListSize", String.valueOf(eventList.size()));
				}
			} while (cursor.moveToPrevious());
		}
		
    	//遍历FoundEvent表
		cursor = db.query("FoundEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if ((cursor.getString(cursor.getColumnIndex("publisher_stu_id")))
						.equals(User.getCurrentUserStuId()))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
    				//Log.d("SearchResult eventListSize", String.valueOf(eventList.size()));
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
	}
	
	//Item项的点击事件
	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		//可以跳转至详细信息界面了
		//Toast.makeText(this, ((Event)view.getItemAtPosition(position))
			//	.getEvent_owner().getUsername(), Toast.LENGTH_SHORT).show();
		//获得点击项的学号并传递到EventDetailsActivity
		Intent intent = new Intent(this, EventDetailsActivity.class);
		String stu_id = ((Event)view.getItemAtPosition(position)).getEvent_owner().getStu_id();
		String data = stu_id + "__3";
		intent.putExtra("extra_data", data);
		Log.d("extra_data in SearchResultActivity", data);
		startActivity(intent);
	}
	
	//下拉刷新
	@Override
	public void onRefresh() {
	}
	
	//加载更多
	@Override
	public void onLoadMore() {
	}
	
	private void onLoad() {
	}

}
