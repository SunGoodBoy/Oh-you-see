package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.Event;
import com.duang.easyecard.util.EventAdapter;
import com.duang.easyecard.util.XListView;
import com.duang.easyecard.util.XListView.IXListViewListener;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchResultActivity extends BaseActivity implements IXListViewListener, OnItemClickListener
{
	private MyDatabaseHelper dbHelper;
	
	private XListView xListView;
	private EventAdapter mAdapter;
	private List<Event> eventList = new ArrayList<Event>();
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		
		geneSearchQueryItems(getIntent());
		
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		xListView = (XListView) findViewById(R.id.xListView_first_fragment);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
		xListView.setAdapter(mAdapter);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);
		mHandler = new Handler();
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
        	String queryString = intent.getStringExtra(SearchManager.QUERY);
        	
        	/*
        	 * 根据queryString依次在数据库的两张Event表中搜索
        	 * 只搜索owner_stu_id和owner_name列
        	 */
        	eventList = new ArrayList<Event>();
        	SQLiteDatabase db = dbHelper.getReadableDatabase();
        	//遍历LostEvent表
    		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
    		if (cursor.moveToLast())
    		{
    			do{
    				if (queryString.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))))
    				{
    					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
        				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
        				event.getEvent_owner().setImageId(R.drawable.app_icon);
        				eventList.add(event);
    				}
    				else if (queryString.equals(cursor.getString(cursor.getColumnIndex("owner_name"))))
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
    				if (queryString.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))))
    				{
    					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
        				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
        				event.getEvent_owner().setImageId(R.drawable.app_icon);
        				eventList.add(event);
    				}
    				else if (queryString.equals(cursor.getString(cursor.getColumnIndex("owner_name"))))
    				{
    					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
        				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
        				event.getEvent_owner().setImageId(R.drawable.app_icon);
        				eventList.add(event);
    				}
    			} while (cursor.moveToPrevious());
    		}
			cursor.close();
			mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
        }
        
	}

	//Item项的点击事件
	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		//可以跳转至详细信息界面了
		Toast.makeText(this, ((Event)view.getItemAtPosition(position))
				.getEvent_owner().getUsername(), Toast.LENGTH_SHORT).show();		
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
