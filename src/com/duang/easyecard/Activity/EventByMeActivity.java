package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.Event;
import com.duang.easyecard.model.User;
import com.duang.easyecard.util.EventAdapter;
import com.duang.easyecard.util.XListView;
import com.duang.easyecard.util.XListView.IXListViewListener;

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
		
		//�򿪻򴴽����ݿ�
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		//��ʼ���ؼ�
		lostEventCheckBox = (CheckBox) findViewById(R.id.event_by_me_check_box_lost);
		foundEventCheckBox = (CheckBox) findViewById(R.id.event_by_me_check_box_found);
		
		//��ʼ����ͼ����ʾȫ���¼�
		doSearchQueryBoth();
		initView();
		
		//��CheckBox���ü����¼�
    	lostEventCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// lostEventCheckBox��ѡ��
					if (foundEventCheckBox.isChecked())
					{
						doSearchQueryBoth();
						initView();
					} else {
						doSearchQueryInLostEvent();
						initView();
					}
				} else {
					// lostEventCheckBoxû�б�ѡ�е����
					if (foundEventCheckBox.isChecked())
					{
						doSearchQueryInFoundEvent();
						initView();
					} else {
						// ��û�б�ѡ��
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
					// foundEventCheckBoxû�б�ѡ�е����
					if (lostEventCheckBox.isChecked())
					{
						doSearchQueryInLostEvent();
						initView();
					} else {
						// ��û�б�ѡ��
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
	
	//�����������������յ�eventList
	private void dontSearchQuery() {
		eventList = new ArrayList<Event>();
		mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
	}

	/*
	 * ��LostEvent��������
	 * ֻ����owner_stu_id��owner_name��
	 */
	private void doSearchQueryInLostEvent() {
		
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	//����LostEvent��
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
	 * ��FoundEvent��������
	 * ֻ����owner_stu_id��owner_name��
	 */
	private void doSearchQueryInFoundEvent() {
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	//����LostEvent��
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
	 * ����LostEvent�������أ�Ҳ��FoundEvent��������
	 * ֻ����owner_stu_id��owner_name��
	 */
	private void doSearchQueryBoth() {
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	//����LostEvent��
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
		
    	//����FoundEvent��
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
	
	//Item��ĵ���¼�
	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		//������ת����ϸ��Ϣ������
		//Toast.makeText(this, ((Event)view.getItemAtPosition(position))
			//	.getEvent_owner().getUsername(), Toast.LENGTH_SHORT).show();
		//��õ�����ѧ�Ų����ݵ�EventDetailsActivity
		Intent intent = new Intent(this, EventDetailsActivity.class);
		String stu_id = ((Event)view.getItemAtPosition(position)).getEvent_owner().getStu_id();
		String data = stu_id + "__3";
		intent.putExtra("extra_data", data);
		Log.d("extra_data in SearchResultActivity", data);
		startActivity(intent);
	}
	
	//����ˢ��
	@Override
	public void onRefresh() {
	}
	
	//���ظ���
	@Override
	public void onLoadMore() {
	}
	
	private void onLoad() {
	}

}
