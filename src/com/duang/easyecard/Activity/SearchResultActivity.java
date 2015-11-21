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
		
		//��ʼ���ؼ�
		lostEventCheckBox = (CheckBox) findViewById(R.id.search_result_check_box_lost);
		foundEventCheckBox = (CheckBox) findViewById(R.id.search_result_check_box_found);
		
		//�򿪻򴴽����ݿ�
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

	//����Item��
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
        	//��ȡ��������
        	queryString = intent.getStringExtra(SearchManager.QUERY);
        	Log.d("QureyString", queryString);
        	
        	tvSearchResultTitle = (TextView) findViewById(R.id.text_search_result_title);
        	tvSearchResultTitle.setText("�������ݣ�" + queryString);
        	
        	//��ʼ����ͼ����ʾȫ���¼�
    		doSearchQueryBoth(queryString);
    		initView();
        	
        	//������CheckBox���ü����¼�
    		lostEventCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			
    			@Override
    			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    				if (isChecked) {
    					// lostEventCheckBox��ѡ��
    					if (foundEventCheckBox.isChecked())
    					{
    						doSearchQueryBoth(queryString);
    						initView();
    					} else {
    						doSearchQueryInLostEvent(queryString);
    						initView();
    					}
    				} else {
    					// lostEventCheckBoxû�б�ѡ�е����
    					if (foundEventCheckBox.isChecked())
    					{
    						doSearchQueryInFoundEvent(queryString);
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
    						doSearchQueryBoth(queryString);
    						initView();
    					} else {
    						doSearchQueryInFoundEvent(queryString);
    						initView();
    					}
    				} else {
    					// foundEventCheckBoxû�б�ѡ�е����
    					if (lostEventCheckBox.isChecked())
    					{
    						doSearchQueryInLostEvent(queryString);
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
        
	}
	
	//������
	private void dontSearchQuery() {
		eventList = new ArrayList<Event>();
		mAdapter = new EventAdapter(this, eventList, R.layout.list_item);
	}
	
	//����LostEvent������
	private void doSearchQueryInLostEvent(String queryString) {
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	//����LostEvent��
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
	
	//����FoundEvent�н�������
	private void doSearchQueryInFoundEvent(String queryString) {
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	//����LostEvent��
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
	 * ����queryString���������ݿ������Event��������
	 * ֻ����owner_stu_id��owner_name��
	 */
	private void doSearchQueryBoth(String queryString) {
		
		Log.d("SearchResultActivity doSearchQuery", "doSearchQuery is called.");
		
		eventList = new ArrayList<Event>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Log.d("SearchResultActivity doSearchQuery", "get database");
    	//����LostEvent��
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
		
		//����FoundEvent��
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

	//activity�����ö�
	@Override 
    protected void onNewIntent(Intent intent) 
	{  
        super.onNewIntent(intent); 
        geneSearchQueryItems(intent); 
    } 
	
	//����ˢ��
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
	
	//���ظ���
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
		xListView.setRefreshTime("�ո�");
	}
}
