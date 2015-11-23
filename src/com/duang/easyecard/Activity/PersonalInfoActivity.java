package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.PersonalInfo;
import com.duang.easyecard.model.User;
import com.duang.easyecard.util.PersonalInfoAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PersonalInfoActivity extends BaseActivity implements  OnItemClickListener
{
	private MyDatabaseHelper dbHelper;

	private ListView listView;
	private PersonalInfoAdapter mAdapter;
	private List<PersonalInfo> personalInfoList = new ArrayList<PersonalInfo>();
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info);
		
		// �򿪻򴴽����ݿ�
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
					personalInfo.setType(0);
					personalInfo.setTitle("�û�ͷ��");
					personalInfo.setImgId(R.drawable.app_icon);
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("ѧ��");
					personalInfo.setType(1);
					personalInfo.setTitle("ѧ��");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("stu_id")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("����");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("name")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("��ʵ����");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("real_name")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("�Ա�");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("gender")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("�꼶");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("grade")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("ѧԺ");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("college")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("ϵ��");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("department")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("��ϵ��ʽ");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("contact")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("����");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("email")));
					personalInfoList.add(personalInfo);
					
					Log.d("In PersonalInfoActivity", "The entity is ready");
					break;
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		mAdapter = new PersonalInfoAdapter(this, personalInfoList);
	}

	private void initView() 
	{
		//Log.d("initView", "start initView");
		listView = (ListView) findViewById(R.id.listView_personal_info);
		mAdapter = new PersonalInfoAdapter(this, personalInfoList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
		mHandler = new Handler();
	}
	
	/**
	 *  ����position��ֵ�������޸���Ϣ��Ӧ��Activity
	 *  intent��ҪЯ��Ҫ�޸ĵ����ݽ����޸�
	 *  �޸���ɺ���ҪЯ���޸ĺ����Ϣ����
	 */
	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		
		switch (position)
		{
		case 0:
			// �޸�ͷ��
			Log.d("PersoanlInfo item", "ChangeUserpic");
			break;
		case 1:
			break;
		case 4:
			// ͨ��AlertDialog�����û�ѡ���޸��Ա�
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("�����Ա�");
			dialog.setMessage("��ѡ�������Ա�");
			dialog.setPositiveButton("��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// ���޸ĺ���Ա��С�д�����ݿ�
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					db.execSQL("update UserInfo set gender = ? where stu_id = ?",
							new String[]{"��", User.getCurrentUserStuId()});
					db.close();
				}
			});
			dialog.setNegativeButton("Ů", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// ���޸ĺ���Ա��С�д�����ݿ�
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					db.execSQL("update UserInfo set gender = ? where stu_id = ?",
							new String[]{"Ů", User.getCurrentUserStuId()});
					db.close();
				}
			});
			dialog.show();
			refreshPersonalInfoList();	//ˢ��listView
			break;
		default:
			// ����Ӧλ�õ�title��content����ChangePersonalInfo��position��Ϊ������
			String title = ((PersonalInfo) view.getItemAtPosition(position)).getTitle();
			String content = ((PersonalInfo) view.getItemAtPosition(position)).getContent();
			Log.d("title-content at position", title + "-" + content + " at " + position);
			Intent intent = new Intent(this, ChangePersonalInfoActivity.class);
			intent.putExtra("title-content", title + "-" + content);
			startActivityForResult(intent, position);
			break;
		}
	}

	// ���� personalInfoList
	private void refreshPersonalInfoList() {
		Log.d("In PersonalInfoActivity", "refreshPersonalInfoList");
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				personalInfoList.clear();
				geneItems(User.getCurrentUserStuId());
				mAdapter = new PersonalInfoAdapter(getApplicationContext(), personalInfoList);
				listView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			}
		}, 1000);
	}
	
	// ����ChangePersonalInfoActivity�ķ��ؽ��
	@Override
	protected void onActivityResult(int requesCode, int resultCode, Intent data) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String content = data.getStringExtra("content_return");
		switch (requesCode)
		{
		case 2:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set name = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
				break;
			}
		case 3:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set real_name = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
				break;
			}
		case 5:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set grade = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
				break;
			}
		case 6:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set college = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
				break;
			}
		case 7:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set department = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
				break;
			}
		case 8:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set contact = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
				break;
			}
		case 9:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set email = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
				break;
			}
		default:
			break;
		}
		db.close();
		refreshPersonalInfoList();	//ˢ��listView
	}

}
