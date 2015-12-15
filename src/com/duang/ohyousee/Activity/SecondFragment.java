package com.duang.ohyousee.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.ohyousee.db.MyDatabaseHelper;
import com.duang.ohyousee.model.Event;
import com.duang.ohyousee.util.EventAdapter;
import com.duang.ohyousee.util.XListView;
import com.duang.ohyousee.util.XListView.IXListViewListener;

import android.content.Intent;
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
				// 只显示未关闭的事件
				if (cursor.getString(cursor.getColumnIndex("close_flag")).equals("0")) {
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
					event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
					event.getEvent_owner().setImageId(R.drawable.default_userpic);
					eventList.add(event);
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
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
		//跳转至详细信息界面
		//Toast.makeText(this.getActivity(), ((Event)view.getItemAtPosition(position))
			//	.getEvent_owner().getUsername(), Toast.LENGTH_SHORT).show();
		//获得点击项的学号并传递到EventDetailsActivity
		Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
		String stu_id = ((Event)view.getItemAtPosition(position)).getEvent_owner().getStu_id();
		String data = stu_id + "__2";
		intent.putExtra("extra_data", data);
		Log.d("extra_data in SecondFragment", data);
		startActivity(intent);
	}
	
}
