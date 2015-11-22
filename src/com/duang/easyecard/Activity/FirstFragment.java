package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.Event;
import com.duang.easyecard.util.EventAdapter;
import com.duang.easyecard.util.XListView;
import com.duang.easyecard.util.XListView.IXListViewListener;

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
import android.widget.Toast;


public class FirstFragment extends Fragment implements IXListViewListener, OnItemClickListener{
	
	private MyDatabaseHelper dbHelper;

	private View viewFragment;
	private XListView xListView = null;
	private EventAdapter mAdapter;
	private List<Event> eventList = new ArrayList<Event>();
	private Handler mHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		viewFragment = inflater.inflate(R.layout.first, null);
		//�򿪻򴴽����ݿ�
		dbHelper = new MyDatabaseHelper(this.getActivity(), "EasyEcard.db", null, 1);
		geneItems();
		initViews();
		return viewFragment;
	}
	
	
	//��ʼ��ListView
	private void initViews(){
		xListView = (XListView) viewFragment.findViewById(R.id.xListView_first_fragment);
		xListView.setPullLoadEnable(true);
		mAdapter = new EventAdapter(this.getActivity(), eventList, R.layout.list_item);
		xListView.setAdapter(mAdapter);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);
		mHandler = new Handler();
	}

	//����Item��
	private void geneItems() {
		eventList = new ArrayList<Event>();
		//�����ݿ���ȡ������������
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				// ֻ��ʾδ�رյ��¼�
				if (cursor.getString(cursor.getColumnIndex("close_flag")).equals("0")) {
					Log.d("close_flag in firstfragment", cursor.getString(cursor.getColumnIndex("close_flag")));
					Event event = new Event(cursor.getString(cursor.getColumnIndex("owner_stu_id")));
					event.getEvent_owner().setUsername(cursor.getString(cursor.getColumnIndex("owner_name")));
					event.getEvent_owner().setImageId(R.drawable.app_icon);
					eventList.add(event);
				}
				
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		mAdapter = new EventAdapter(this.getActivity(), eventList, R.layout.list_item);
	}
	
	//ˢ��
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
	
	//���ظ���
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
		xListView.setRefreshTime("�ո�");
	}
	
	//Item�ĵ�������¼�
	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		//��ת����ϸ��Ϣ����
		//Toast.makeText(this.getActivity(), ((Event)view.getItemAtPosition(position))
				//.getEvent_owner().getUsername(), Toast.LENGTH_SHORT).show();
		
		//��õ�����ѧ�Ų����ݵ�EventDetailsActivity
		Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
		String stu_id = ((Event)view.getItemAtPosition(position)).getEvent_owner().getStu_id();
		String data = stu_id + "__1";
		intent.putExtra("extra_data", data);
		Log.d("extra_data in FirstFragment", data);
		startActivity(intent);
	}
	
}
