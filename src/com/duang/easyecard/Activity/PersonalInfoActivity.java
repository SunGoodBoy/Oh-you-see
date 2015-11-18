package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.PersonalInfo;
import com.duang.easyecard.model.User;
import com.duang.easyecard.util.PersonalInfoImageAdapter;
import com.duang.easyecard.util.PersonalInfoTextAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PersonalInfoActivity extends BaseActivity implements  OnItemClickListener
{
	private MyDatabaseHelper dbHelper;

	private ListView ListView1;
	private ListView ListView2;
	private PersonalInfoImageAdapter mAdapter1;
	private PersonalInfoTextAdapter mAdapter2;
	private List<PersonalInfo> infoList1 = new ArrayList<PersonalInfo>();
	private List<PersonalInfo> infoList2 = new ArrayList<PersonalInfo>();
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info);
		
		//�򿪻򴴽����ݿ�
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		geneItems(User.getCurrentUserStuId());
		
		initView();
		
	}

	private void geneItems(String stu_id) 
	{
		infoList1 = new ArrayList<PersonalInfo>();
		infoList2 = new ArrayList<PersonalInfo>();
		
		//�����ݿ���ȡ������������
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("UserInfo", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if (User.getCurrentUserStuId().equals(cursor.getString(cursor.getColumnIndex("stu_id"))))
				{
					PersonalInfo info = new PersonalInfo("�û�ͷ��");
					info.setImgId(R.drawable.app_icon);
					infoList1.add(info);
					
					info = new PersonalInfo("ѧ��");
					info.setContent(cursor.getString(cursor.getColumnIndex("stu_id")));
					infoList2.add(info);
					
					info = new PersonalInfo("����");
					info.setContent(cursor.getString(cursor.getColumnIndex("name")));
					infoList2.add(info);
					
					info = new PersonalInfo("��ʵ����");
					info.setContent(cursor.getString(cursor.getColumnIndex("real_name")));
					infoList2.add(info);
					
					info = new PersonalInfo("�Ա�");
					info.setContent(cursor.getString(cursor.getColumnIndex("gender")));
					infoList2.add(info);
					
					info = new PersonalInfo("�꼶");
					info.setContent(cursor.getString(cursor.getColumnIndex("grade")));
					infoList2.add(info);
					
					info = new PersonalInfo("ѧԺ");
					info.setContent(cursor.getString(cursor.getColumnIndex("college")));
					infoList2.add(info);
					
					info = new PersonalInfo("ϵ��");
					info.setContent(cursor.getString(cursor.getColumnIndex("department")));
					infoList2.add(info);
					
					info = new PersonalInfo("��ϵ��ʽ");
					info.setContent(cursor.getString(cursor.getColumnIndex("contact")));
					infoList2.add(info);
					
					info = new PersonalInfo("����");
					info.setContent(cursor.getString(cursor.getColumnIndex("email")));
					infoList2.add(info);
					
					break;
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		mAdapter1 = new PersonalInfoImageAdapter(this, infoList1, R.layout.personal_info_list_item_image);
		mAdapter2 = new PersonalInfoTextAdapter(this, infoList2, R.layout.personal_info_list_item_text);
	}

	private void initView() 
	{
		ListView1 = (ListView) findViewById(R.id.listView_personal_info_img);
		mAdapter1 = new PersonalInfoImageAdapter(this, infoList1, R.layout.personal_info_list_item_image);
		ListView1.setAdapter(mAdapter1);
		ListView2 = (ListView) findViewById(R.id.listView_personal_info_text);
		mAdapter2 = new PersonalInfoTextAdapter(this, infoList2, R.layout.personal_info_list_item_text);
		ListView2.setAdapter(mAdapter2);
		ListView1.setOnItemClickListener(this);
		ListView2.setOnItemClickListener(this);
		mHandler = new Handler();
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}



}
