package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.PersonalInfo;
import com.duang.easyecard.model.User;
import com.duang.easyecard.util.PersonalInfoAdapter;
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

	private ListView ListView;
	private PersonalInfoAdapter mAdapter;
	private List<PersonalInfo> personalInfoList = new ArrayList<PersonalInfo>();
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info);
		
		//�򿪻򴴽����ݿ�
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		geneItems(User.getCurrentUserStuId());
		
		initView();
		
	}

	private void geneItems(String stu_id) 
	{
		personalInfoList = new ArrayList<PersonalInfo>();
		
		//�����ݿ���ȡ������������
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("UserInfo", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("stu_id"))))
				{
					PersonalInfo personalInfo = new PersonalInfo("�û�ͷ��");
					personalInfo.setImgId(R.drawable.app_icon);
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("ѧ��");
					personalInfo.setTitle("ѧ��");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("stu_id")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("����");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("name")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("��ʵ����");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("real_name")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("�Ա�");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("gender")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("�꼶");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("grade")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("ѧԺ");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("college")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("ϵ��");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("department")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("��ϵ��ʽ");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("contact")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("����");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("email")));
					personalInfoList.add(personalInfo);
					
					break;
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		mAdapter = new PersonalInfoAdapter(this, personalInfoList, R.layout.personal_info);
	}

	private void initView() 
	{
		ListView = (ListView) findViewById(R.id.listView_personal_info);
		mAdapter = new PersonalInfoAdapter(this, personalInfoList, R.layout.personal_info);
		ListView.setAdapter(mAdapter);
		ListView.setOnItemClickListener(this);
		mHandler = new Handler();
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}



}
