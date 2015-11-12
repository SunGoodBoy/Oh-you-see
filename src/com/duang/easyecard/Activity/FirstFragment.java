package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.model.Event;
import com.duang.easyecard.model.User;
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
	private int start = 0;
	private static int refreshCnt = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		viewFragment = inflater.inflate(R.layout.first, null);
		geneItems();
		initViews();
		return viewFragment;
	}
	
	//��������
	private List<Event> getLists()	{
		List<Event> userList = new ArrayList<Event>();
		for (int i = 0; i < 20; i++)	{
			Event event = new Event();
			event.getEvent_owner().setImageId(R.drawable.ic_launcher);
			event.getEvent_owner().setStu_id("ѧ��" + i);
			event.getEvent_owner().setUsername("����");
		}
		return userList;
	}
	
	private void initViews(){
		xListView=(XListView) viewFragment.findViewById(R.id.xListView);
		xListView.setPullLoadEnable(true);
		mAdapter = new EventAdapter(this.getActivity(), eventList);
		xListView.setAdapter(mAdapter);
		xListView.setXListViewListener(this);
		mHandler = new Handler();
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;
				eventList.clear();
				geneItems();
				//mAdapter = new UserAdaper(getActivity(), userList);
				xListView.setAdapter(new EventAdapter(getActivity(), eventList));
				onLoad();
			}
		}, 2000);
	}
	

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems();
				//mAdapter.notifyDataSetChanged();
				
				onLoad();
			}
		}, 2000);
	}
	
	//�����µ��б����������String�ĳ��˶�����ʱ������
	private void geneItems() {
		Event event = new Event();
		event.getEvent_owner().setImageId(R.drawable.ic_launcher);
		event.getEvent_owner().setStu_id("ѧ��");
		event.getEvent_owner().setUsername("����");
		eventList.add(event);
	}

	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime("��ʱ��֪��");
	}
	
}
