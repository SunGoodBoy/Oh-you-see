package com.duang.easyecard.Activity;

import java.util.Calendar;
import java.util.StringTokenizer;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.User;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlterEventActivity extends BaseActivity {

	private MyDatabaseHelper dbHelper;
	
	private EditText mStuId;
	private EditText mName;
	private EditText mContact;
	private EditText mEventPlace;
	private EditText mDescription;
	
	private TextView mEventPlaceTitle;
	private TextView mEventDateTitle;
	private TextView mEventTimeTitle;
	
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;
	private Button mSubmit_button;
	private Button mCancel_button;
	
	private int FLAG; // ���ڱ�ʶ�¼������ͣ�LostEventΪ1��FoundEventΪ2
	
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_lost_event);
		
		mStuId = (EditText) findViewById(R.id.add_lost_event_stu_id_edit);
		mName = (EditText) findViewById(R.id.add_lost_event_name_edit);
		mContact = (EditText) findViewById(R.id.add_lost_event_contact_edit);
		mEventPlace = (EditText) findViewById(R.id.add_event_lost_place_edit);
		mDescription = (EditText) findViewById(R.id.add_event_lost_lost_decription_edit);
		
		mEventPlaceTitle = (TextView) findViewById(R.id.add_event_lost_place);
		mEventDateTitle = (TextView) findViewById(R.id.add_event_lost_date_text);
		mEventTimeTitle = (TextView) findViewById(R.id.add_event_lost_time_text);
		
		mDatePicker = (DatePicker) findViewById(R.id.add_event_lost_date_picker);
		mTimePicker = (TimePicker) findViewById(R.id.add_event_lost_time_picker);
		
		mSubmit_button = (Button) findViewById(R.id.add_lost_event_submit);
		mCancel_button = (Button) findViewById(R.id.add_lost_event_cancel);
		
		mTimePicker.setIs24HourView(true);	//����ʱ��Ϊ24Сʱ��
		mSubmit_button.setText("�����޸�");
		
		//�򿪻򴴽����ݿ�
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		initViews();
	}
	
	private void initViews() {
		
		Intent intent = getIntent();
		String data = intent.getStringExtra("stu_id__FLAG");

		// ��ȡ�¼�ʧ����ѧ��
		String split = "_";
		StringTokenizer token = new StringTokenizer(data, split);
		String stu_id = token.nextToken();
		Log.d("AlterEventActivity stu_id in data", stu_id);
		// ��ȡ�¼�������
		FLAG = Integer.parseInt(token.nextToken());
		Log.d("AlterEventActivity FLAG in data", String.valueOf(FLAG));
		
		// ��ʾѧ��
		mStuId.setText(stu_id);
		
		switch (FLAG) {
			case 1:
				getInfoFromLostEvent(stu_id);
				break;
			case 2:
				getInfoFromFoundEvent(stu_id);
				break;
			default:
				break;
		}
		
		
		
		//�ύ��ť����¼�
		mSubmit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alterEventMain(v);
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				Toast.makeText(AlterEventActivity.this, "�޸ĳɹ���", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		//��ȡ������ť�ĵ���¼�
		mCancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});
	}
	
	private void getInfoFromFoundEvent(String stu_id) {
		// ��FoundEvent��������Ϣ
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("FoundEvent", null, null, null, null, null, null);
		
		if (cursor.moveToLast())
		{
			do{
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))) ||
						cursor.getString(cursor.getColumnIndex("close_flag")).equals("0"))
				{
					// ʧ������
					mName.setText(cursor.getString(cursor.getColumnIndex("owner_name")));
					// ʧ����ϵ��ʽ
					mContact.setText(cursor.getString(cursor.getColumnIndex("owner_contact")));
					// ��ʾΪ������ʰ�����ڡ�
					mEventDateTitle.setText("����ʰ������");
					// ��ȡʰ������
					String date = cursor.getString(cursor.getColumnIndex("found_date"));
					String split = "-";
					StringTokenizer token = new StringTokenizer(date, split);
					int year = 0, month = 0, day = 0;
					if (token.hasMoreTokens())
					{
						year = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						month = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						day = Integer.parseInt(token.nextToken());
					}
					// ��ʼ��DatePicker
					mDatePicker.init(year, month, day, new OnDateChangedListener() {
						@Override
						public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
							// TODO Auto-generated method stub
						}
					});
					// ��ʾΪ������ʰ��ʱ�䡱
					mEventTimeTitle.setText("����ʰ��ʱ��");
					// ��ȡ��ʧʱ��
					String time_in_day = cursor.getString(cursor.getColumnIndex("found_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					int hour = 0, minute = 0;
					if (token.hasMoreTokens())
					{
						hour = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						minute = Integer.parseInt(token.nextToken());
					}
					// ��ʼ��TimePicker
					mTimePicker.setCurrentHour(hour);
					mTimePicker.setCurrentMinute(minute);
					//��ʾΪ����ʧ�ص㡱
					mEventPlaceTitle.setText("ʰ��ص�");
					mEventPlace.setText(cursor.getString(cursor.getColumnIndex("found_place")));
					//��ʾ����
					mDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
	}

	private void getInfoFromLostEvent(String stu_id) {
		// ��LostEvent��������Ϣ
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		
		if (cursor.moveToLast())
		{
			do{
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))) ||
						cursor.getString(cursor.getColumnIndex("close_flag")).equals("0"))
				{
					// ʧ������
					mName.setText(cursor.getString(cursor.getColumnIndex("owner_name")));
					// ʧ����ϵ��ʽ
					mContact.setText(cursor.getString(cursor.getColumnIndex("owner_contact")));
					// ��ʾΪ�����ö�ʧ���ڡ�
					mEventDateTitle.setText("���ö�ʧ����");
					// ��ȡ��ʧ����
					String date = cursor.getString(cursor.getColumnIndex("lost_date"));
					String split = "-";
					StringTokenizer token = new StringTokenizer(date, split);
					int year = 0, month = 0, day = 0;
					if (token.hasMoreTokens())
					{
						year = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						month = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						day = Integer.parseInt(token.nextToken());
					}
					// ��ʼ��DatePicker
					mDatePicker.init(year, month, day, new OnDateChangedListener() {
						@Override
						public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
							// TODO Auto-generated method stub
						}
					});
					// ��ʾΪ�����ö�ʧʱ�䡱
					mEventTimeTitle.setText("���ö�ʧʱ��");
					// ��ȡ��ʧʱ��
					String time_in_day = cursor.getString(cursor.getColumnIndex("lost_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					int hour = 0, minute = 0;
					if (token.hasMoreTokens())
					{
						hour = Integer.parseInt(token.nextToken());
					}
					if (token.hasMoreTokens())
					{
						minute = Integer.parseInt(token.nextToken());
					}
					// ��ʼ��TimePicker
					mTimePicker.setCurrentHour(hour);
					mTimePicker.setCurrentMinute(minute);
					//��ʾΪ����ʧ�ص㡱
					mEventPlaceTitle.setText("��ʧ�ص�");
					mEventPlace.setText(cursor.getString(cursor.getColumnIndex("lost_place")));
					//��ʾ����
					mDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
	}

	private void alterEventMain(View v)	{
		
		
	}

	// ������д�����ݿ�
	private void writeDataToDb() {
		
		
	}
	
	// ����Back��ť�ĵ��
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)	{
			// ��������Ϣ�Ƿ��޸ģ�ֱ�����ٻ
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		}
		return false;
	}
}
