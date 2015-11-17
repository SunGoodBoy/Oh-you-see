package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.model.Event;
import com.duang.easyecard.model.PersonalInfo;
import com.duang.easyecard.util.EventAdapter;
import com.duang.easyecard.util.PersonalInfoAdapter;
import com.duang.easyecard.util.XListView;
import com.duang.easyecard.util.XListView.IXListViewListener;

import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class PersonalInfoActivity extends BaseActivity implements IXListViewListener, OnItemClickListener
{

	private XListView xListView;
	private PersonalInfoAdapter mAdapter;
	private List<PersonalInfo> infotList = new ArrayList<PersonalInfo>();
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info);
		
		geneItems();
		
		initView();
		
	}

	private void geneItems() 
	{
		infotList = new ArrayList<PersonalInfo>();
		
	}

	private void initView() 
	{
		xListView = (XListView) findViewById(R.id.xListView_personal_info);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		mAdapter = new PersonalInfoAdapter(this, infotList, R.layout.personal_info_list_item);
		xListView.setAdapter(mAdapter);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);
		mHandler = new Handler();
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}


}
