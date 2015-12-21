package com.duang.ohyousee.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.ohyousee.R;
import com.duang.ohyousee.db.MyDatabaseHelper;
import com.duang.ohyousee.model.Event;
import com.duang.ohyousee.util.EventAdapter;
import com.duang.ohyousee.util.XListView;
import com.duang.ohyousee.util.XListView.IXListViewListener;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SearchResultActivity extends BaseActivity implements IXListViewListener, OnItemClickListener
{
	private MyDatabaseHelper dbHelper;
	
	private XListView xListView;
	private EventAdapter mAdapter;
	private List<Event> eventList = new ArrayList<Event>();
	private Handler mHandler;
	
	private String queryString;
	
	private TextView tvSearchResultTitle;
	private CheckBox lostEventCheckBox;
	private CheckBox foundEventCheckBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		
		//初始化控件
		lostEventCheckBox = (CheckBox) findViewById(R.id.search_result_check_box_lost);
		foundEventCheckBox = (CheckBox) findViewById(R.id.search_result_check_box_found);
		
		//打开或创建数据库
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		geneSearchQueryItems(getIntent());
		
	}

	private void initView() {
		Log.d("SearchResultActivity doSearchQuery", "initView is called");
		xListView = (XListView) findViewById(R.id.xListView_search_result);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
		xListView.setAdapter(mAdapter);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);
		mHandler = new Handler();
		Log.d("SearchResultActivity doSearchQuery", "initView called finished");
	}

	//生成Item项
	private void geneSearchQueryItems(Intent intent) 
	{
		if (intent == null)
		{
			Log.e("geneSearchQueryItems intent", "Fail to get intent");
			return;
		}
		
		String queryAction = intent.getAction(); 
        if( Intent.ACTION_SEARCH.equals(queryAction))
        {
        	//获取搜索内容
        	queryString = intent.getStringExtra(SearchManager.QUERY);
        	Log.d("QureyString", queryString);
        	
        	tvSearchResultTitle = (TextView) findViewById(R.id.text_search_result_title);
        	tvSearchResultTitle.setText("搜索内容：" + queryString);
        	
        	//初始化视图，显示全部事件
    		doSearchQueryBoth(queryString);
    		initView();
        	
        	//对两个CheckBox设置监听事件
    		lostEventCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			
    			@Override
    			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    				if (isChecked) {
    					// lostEventCheckBox被选中
    					if (foundEventCheckBox.isChecked())
    					{
    						doSearchQueryBoth(queryString);
    						initView();
    					} else {
    						doSearchQueryInLostEvent(queryString);
    						initView();
    					}
    				} else {
    					// lostEventCheckBox没有被选中的情况
    					if (foundEventCheckBox.isChecked())
    					{
    						doSearchQueryInFoundEvent(queryString);
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
    						doSearchQueryBoth(queryString);
    						initView();
    					} else {
    						doSearchQueryInFoundEvent(queryString);
    						initView();
    					}
    				} else {
    					// foundEventCheckBox没有被选中的情况
    					if (lostEventCheckBox.isChecked())
    					{
    						doSearchQueryInLostEvent(queryString);
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
        
	}
	
	//不搜索
	private void dontSearchQuery() {
		eventList = new ArrayList<Event>();
		mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
	}
	
	//仅在LostEvent中搜索
	private void doSearchQueryInLostEvent(String queryString) {
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	//遍历LostEvent表
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if ((cursor.getString(cursor.getColumnIndex("owner_stu_id"))).contains(queryString))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
    				//Log.d("SearchResult eventListSize", String.valueOf(eventList.size()));
				}
				else if ((cursor.getString(cursor.getColumnIndex("owner_name"))).contains(queryString))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
				}
			} while (cursor.moveToPrevious());
			cursor.close();
			db.close();
			mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
		}
	}
	
	//仅在FoundEvent中进行搜索
	private void doSearchQueryInFoundEvent(String queryString) {
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	//遍历LostEvent表
		Cursor cursor = db.query("FoundEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if ((cursor.getString(cursor.getColumnIndex("owner_stu_id"))).contains(queryString))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
    				//Log.d("SearchResult eventListSize", String.valueOf(eventList.size()));
				}
				else if ((cursor.getString(cursor.getColumnIndex("owner_name"))).contains(queryString))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
				}
			} while (cursor.moveToPrevious());
			cursor.close();
			db.close();
			mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
		}
	}

	/*
	 * 根据queryString依次在数据库的两张Event表中搜索
	 * 只搜索owner_stu_id和owner_name列
	 */
	private void doSearchQueryBoth(String queryString) {
		
		Log.d("SearchResultActivity doSearchQuery", "doSearchQuery is called.");
		
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Log.d("SearchResultActivity doSearchQuery", "get database");
    	//遍历LostEvent表
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if ((cursor.getString(cursor.getColumnIndex("owner_stu_id"))).contains(queryString))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
    				//Log.d("SearchResult eventListSize", String.valueOf(eventList.size()));
				}
				else if ((cursor.getString(cursor.getColumnIndex("owner_name"))).contains(queryString))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
				}
			} while (cursor.moveToPrevious());
		}
		
		//遍历FoundEvent表
		cursor = db.query("FoundEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if ((cursor.getString(cursor.getColumnIndex("owner_stu_id"))).contains(queryString))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
				}
				else if ((cursor.getString(cursor.getColumnIndex("owner_name"))).contains(queryString))
				{
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
    				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
    				event.getEvent_owner().setImageId(R.drawable.app_icon);
    				eventList.add(event);
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

	//activity重新置顶
	@Override 
    protected void onNewIntent(Intent intent) 
	{  
        super.onNewIntent(intent); 
        geneSearchQueryItems(intent); 
    } 
	
	//下拉刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				eventList.clear();
				geneSearchQueryItems(getIntent());
				mAdapter = new EventAdapter(SearchResultActivity.this, eventList, R.layout.list_item);
				xListView.setAdapter(mAdapter);
				onLoad();
			}
		}, 2000);
	}
	
	//加载更多
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneSearchQueryItems(getIntent());
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	
	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime("刚刚");
	}
}
