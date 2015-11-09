package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.model.User;
import com.duang.easyecard.util.UserAdapter;
import com.duang.easyecard.util.XListView;
import com.duang.easyecard.util.XListView.IXListViewListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class FirstFragment extends Fragment implements IXListViewListener{

	private View viewFragment;
	private XListView xListView = null;
	private UserAdapter mAdapter;
	private List<User> userList = getLists();
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		viewFragment = inflater.inflate(R.layout.first, null);
		geneItems();
		initViews();
		return viewFragment;
	}
	
	//返回数据
	private List<User> getLists()	{
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i < 20; i++)	{
			User user = new User();
			user.setImageId(R.drawable.ic_launcher);
			user.setStu_id("学号" + i);
			user.setUsername("姓名");
		}
		return userList;
	}
	
	private void initViews(){
		xListView=(XListView) viewFragment.findViewById(R.id.xListView);
		xListView.setPullLoadEnable(true);
		mAdapter = new UserAdapter(this.getActivity(), userList);
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
				userList.clear();
				geneItems();
				//mAdapter = new UserAdaper(getActivity(), userList);
				xListView.setAdapter(new UserAdapter(getActivity(), userList));
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
	
	//生成新的列表项，现在由于String改成了对像，暂时不会了
	private void geneItems() {
		User user = new User();
		user.setImageId(R.drawable.ic_launcher);
		user.setStu_id("学号");
		user.setUsername("姓名");
		userList.add(user);
	}

	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime("暂时不知道");
	}
	
}
