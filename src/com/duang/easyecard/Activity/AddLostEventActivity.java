package com.duang.easyecard.Activity;

import java.util.Calendar;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.User;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
	private Button mSubmit_button;
	private Button mCancel_button;
	
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_lost_event);
		
		mStu_id = (EditText) findViewById(R.id.add_lost_event_stu_id_edit);
		mName = (EditText) findViewById(R.id.add_lost_event_name_edit);
		mContact = (EditText) findViewById(R.id.add_lost_event_contact_edit);
		mLost_place = (EditText) findViewById(R.id.add_event_lost_place_edit);
		mDescription = (EditText) findViewById(R.id.add_event_lost_lost_decription_edit);
		
		mDatePicker = (DatePicker) findViewById(R.id.add_event_lost_date_picker);
		mTimePicker = (TimePicker) findViewById(R.id.add_event_lost_time_picker);
		
		mSubmit_button = (Button) findViewById(R.id.add_lost_event_submit);
		mCancel_button = (Button) findViewById(R.id.add_lost_event_cancel);
		
		mTimePicker.setIs24HourView(true);	//����ʱ��Ϊ24Сʱ��
		
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
				if (!hasRepeatedEvent(stu_id)) {
					if (!name.isEmpty())	{
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
					else	{
						Toast.makeText(AddLostEventActivity.this, "��������Ϊ�գ�", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Toast.makeText(AddLostEventActivity.this, "��ѧ���Ѿ������˶�ʧ��Ϣ��", Toast.LENGTH_SHORT).show();
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

	// ��ѧ�Ŵ����ظ���δ�رյ��¼�
	private boolean hasRepeatedEvent(String stu_id) {
		// ��ÿɶ����ݿ�
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		//����LostEvent��
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		if (cursor.moveToLast()) {
			do {
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))))
				{
					if (cursor.getString(cursor.getColumnIndex("close_flag")).equals("0")) {
						return true;
					}
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		return false;
	}
	
	// ������д�����ݿ�
	private void writeDataToDb() {
		
		//��õ�ǰ�û���Ϊ������
		String publisher = User.getCurrentUserStuId();
		
		String stu_id = mStu_id.getText().toString();
		String name = mName.getText().toString();
		String contact = mContact.getText().toString();
		String lost_place = mLost_place.getText().toString();
		String descrption = mDescription.getText().toString();
		
		//��ϵͳ�ĵ�ǰ���ڴ���add_date����ǰʱ�䴫��add_time
		Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		String add_date = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day_of_month);
		String add_time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + "00";
		
		//��ȡDatePicker�е�����
		year = mDatePicker.getYear();
		month = mDatePicker.getMonth();
		day_of_month = mDatePicker.getDayOfMonth();
		String lost_date = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day_of_month);
		
		/** ��ӡʱ����ȷ���Ƿ���ȷ
		Log.d("year", String.valueOf(year));
		Log.d("month", String.valueOf(month));
		Log.d("day_of_month", String.valueOf(day_of_month));
		Log.d("hour", String.valueOf(hour));
		Log.d("minute", String.valueOf(minute));
		*/
		
		//��ȡTimePicker�е�ʱ��
		hour = mTimePicker.getCurrentHour();
		minute = mTimePicker.getCurrentMinute();
		String lost_time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + "00";
		
		//д�����ݿ�
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("insert into LostEvent ("
				+ "publisher_stu_id, "
				+ "add_date, "
				+ "add_time, "
				+ "owner_stu_id, "
				+ "owner_name, "
				+ "owner_contact, "
				+ "lost_date, "
				+ "lost_time, "
				+ "lost_place, "
				+ "description, "
				+ "duration, "
				+ "found_flag, "
				+ "close_flag)"
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new String[] {publisher, add_date, add_time, stu_id, name, contact, lost_date, lost_time, 
						lost_place, descrption, "30", "0", "0"});
	}
	
}

