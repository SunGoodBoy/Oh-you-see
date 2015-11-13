package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.model.Event;
import com.duang.easyecard.util.EventAdapter;
import com.duang.easyecard.util.XListView;
import com.duang.easyecard.util.XListView.IXListViewListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FirstFragment extends Fragment implements IXListViewListener{

	private View viewFragment;
	private XListView xListView = null;
	private EventAdapter mAdapter;
	private List<Event> eventList = getLists();
	private Handler mHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		viewFragment = inflater.inflate(R.layout.first, null);
		geneItems();
		initViews();
		return viewFragment;
	}
	
	//返回数据
	private List<Event> getLists()	{
		List<Event> eventList = new ArrayList<Event>();
		
		Event event = new Event("test0001001");
		event.getEvent_owner().setImageId(R.drawable.app_icon);
		event.getEvent_owner().setUsername("小二");
		eventList.add(event);
		
		event.getEvent_owner().setStu_id("test0001002");
		event.getEvent_owner().setImageId(R.drawable.app_icon);
		event.getEvent_owner().setUsername("小二");
		eventList.add(event);
		
		event.getEvent_owner().setStu_id("test0001003");
		event.getEvent_owner().setImageId(R.drawable.app_icon);
		event.getEvent_owner().setUsername("小二");
		eventList.add(event);
		event.getEvent_owner().setStu_id("test0001004");
		event.getEvent_owner().setImageId(R.drawable.app_icon);
		event.getEvent_owner().setUsername("小二");
		eventList.add(event);
		
		return eventList;
	}
	
	//初始化ListView
	private void initViews(){
		xListView=(XListView) viewFragment.findViewById(R.id.xListView);
		xListView.setPullLoadEnable(true);
		mAdapter = new EventAdapter(this.getActivity(), eventList, R.layout.list_item);
		xListView.setAdapter(mAdapter);
		xListView.setXListViewListener(this);
		mHandler = new Handler();
	}

	//生成Item项
	private void geneItems() {
		List<Event> eventList = new ArrayList<Event>();
		
		Event event = new Event("test0001001");
		event.getEvent_owner().setImageId(R.drawable.app_icon);
		event.getEvent_owner().setUsername("小二");
		eventList.add(event);
		
		event.getEvent_owner().setStu_id("test0001002");
		event.getEvent_owner().setImageId(R.drawable.app_icon);
		event.getEvent_owner().setUsername("小二");
		eventList.add(event);
		
		event.getEvent_owner().setStu_id("test0001003");
		event.getEvent_owner().setImageId(R.drawable.app_icon);
		event.getEvent_owner().setUsername("小二");
		eventList.add(event);
		event.getEvent_owner().setStu_id("test0001004");
		event.getEvent_owner().setImageId(R.drawable.app_icon);
		event.getEvent_owner().setUsername("小二");
		eventList.add(event);
	}
	
	//刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				eventList.clear();
				geneItems();
				mAdapter = new EventAdapter(FirstFragment.this.getActivity(), eventList, R.layout.list_item);
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
		xListView.setRefreshTime("暂时不知道");
	}
}
