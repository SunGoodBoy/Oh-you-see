package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.Event;
import com.duang.easyecard.util.EventAdapter;
import com.duang.easyecard.util.XListView;
import com.duang.easyecard.util.XListView.IXListViewListener;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class SecondFragment extends Fragment implements IXListViewListener, OnItemClickListener{
	
	private MyDatabaseHelper dbHelper;

	private View viewFragment;
	private XListView xListView = null;
	private EventAdapter mAdapter;
	private List<Event> eventList = new ArrayList<Event>();
	private Handler mHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		viewFragment = inflater.inflate(R.layout.second, null);
		//打开或创建数据库
		dbHelper = new MyDatabaseHelper(this.getActivity(), "EasyEcard.db", null, 1);
		geneItems();
		initViews();
		return viewFragment;
	}
	
	
	//初始化ListView
	private void initViews(){
		xListView = (XListView) viewFragment.findViewById(R.id.xListView_second_fragment);
		xListView.setPullLoadEnable(true);
		mAdapter = new EventAdapter(this.getActivity(), eventList, R.layout.list_item);
		xListView.setAdapter(mAdapter);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);
		mHandler = new Handler();
	}

	//生成Item项
	private void geneItems() {
		eventList = new ArrayList<Event>();
		//从数据库中取出数据生成项
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("FoundEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
				event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
				event.getEvent_owner().setImageId(R.drawable.app_icon);
				eventList.add(event);
				Log.d("eventListSize", String.valueOf(eventList.size()));
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		mAdapter = new EventAdapter(this.getActivity(), eventList, R.layout.list_item);
	}
	
	//刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				eventList.clear();
				geneItems();
				mAdapter = new EventAdapter(SecondFragment.this.getActivity(), eventList, R.layout.list_item);
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
				geneItems();
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
	
	//Item的点击监听事件
	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		//可以跳转至详细信息界面了
		Toast.makeText(this.getActivity(), ((Event)view.getItemAtPosition(position))
				.getEvent_owner().getUsername(), Toast.LENGTH_SHORT).show();
	}
	
}
