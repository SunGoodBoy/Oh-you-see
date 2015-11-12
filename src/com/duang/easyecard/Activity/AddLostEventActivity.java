package com.duang.easyecard.Activity;

import java.util.Calendar;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddLostEventActivity extends BaseActivity	{

	
	private MyDatabaseHelper dbHelper;
	
	private EditText mStu_id;
	private EditText mName;
	private EditText mContact;
	private EditText mLost_place;
	private EditText mDescription;
	
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;
	private Calendar mCalendar;
	
	private Button mSubmit_button;
	private Button mCancel_button;
	
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_lost_event);
		
		mStu_id = (EditText) findViewById(R.id.add_event_stu_id_edit);
		mName = (EditText) findViewById(R.id.add_event_name_edit);
		mContact = (EditText) findViewById(R.id.add_event_contact_edit);
		mLost_place = (EditText) findViewById(R.id.add_event_lost_place_edit);
		mDescription = (EditText) findViewById(R.id.add_event_lost_lost_decription_edit);
		
		mDatePicker = (DatePicker) findViewById(R.id.add_event_lost_date_picker);
		mTimePicker = (TimePicker) findViewById(R.id.add_event_lost_time_picker);
		
		mSubmit_button = (Button) findViewById(R.id.add_event_submit);
		mCancel_button = (Button) findViewById(R.id.add_event_cancel);
		
		//�򿪻򴴽����ݿ�
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		//�ύ��ť����¼�
		mSubmit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addLostEvent_main(v);
			}
		});
		

		//��ȡ������ť�ĵ���¼�
		mCancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void addLostEvent_main(View v)	{
		
		String stu_id = mStu_id.getText().toString();
		String name = mName.getText().toString();
		String contact = mContact.getText().toString();
		
		//�ж�ѧ���Ƿ�Ϊ��
		if (!stu_id.isEmpty())	{
			if (stu_id.length() == 11)	{
				if (!contact.isEmpty())	{
					//������д�����ݿ�
					writeDataToDb();
					finish();
					Toast.makeText(AddLostEventActivity.this, "�ύ�ɹ���", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(AddLostEventActivity.this, "������д��ϵ��ʽ��", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(AddLostEventActivity.this, "ѧ�ű���Ϊ11λ��", Toast.LENGTH_SHORT).show();
			}
		}
		else	{
			Toast.makeText(AddLostEventActivity.this, "ѧ�Ų���Ϊ��", Toast.LENGTH_LONG).show();
		}
	}

	//������д�����ݿ�
	private void writeDataToDb() {
		// TODO Auto-generated method stub
		String stu_id = mStu_id.getText().toString();
		String name = mName.getText().toString();
		String contact = mContact.getText().toString();
		String lost_place = mLost_place.getText().toString();
		String descrption = mDescription.getText().toString();
		
		mCalendar = Calendar.getInstance();
		int lost_year = mCalendar.get(Calendar.YEAR);
		int lost_month = mCalendar.get(Calendar.MONTH);
		int lost_day_of_month = mCalendar.get(Calendar.DAY_OF_MONTH);
		int lost_hour = mCalendar.get(Calendar.HOUR);
		int lost_minute = mCalendar.get(Calendar.MINUTE);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("insert into LostEvent ("
				+ "owner_stu_id, "
				+ "owner_name, "
				+ "owner_contact, "
				+ "lost_date, "
				+ "lost_time, "
				+ "lost_place, "
				+ "description, "
				+ "duration, "
				+ "due_date, "
				+ "found_flag, "
				+ "close_flag)"
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new String[] {
						stu_id, name, contact, "default", "default", lost_place, descrption,
						"10", "default", "0", "0"});
		
		
	}
	
	
	
	
}

