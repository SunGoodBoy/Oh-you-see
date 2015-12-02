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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
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
	
	private int FLAG;  // ���ڱ�ʶ�¼������ͣ�LostEventΪ1��FoundEventΪ2
	private String mOldStuId;  // OldStuId����ԭѧ��
	private String mPublisher = User.getCurrentUserStuId();  // ������
	
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
		mOldStuId = stu_id;
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
				Intent intent = new Intent(AlterEventActivity.this, EventDetailsActivity.class);
				intent.putExtra("extra_data", mStuId.getText().toString() + "__" + FLAG);
				startActivity(intent);
				finish();
			}
		});
		//��ȡ������ť�ĵ���¼�
		mCancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
					Log.d("year-month-day", year + "-" + month + "-" + day);
					// ����DatePickerĬ������
					mDatePicker.updateDate(year, month - 1, day);
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
					Log.d("hour:minute", hour + ":" + minute);
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
					Log.d("year-month-day", year + "-" + month + "-" + day);
					// ����DatePickerĬ������
					mDatePicker.updateDate(year, month - 1, day);
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
					Log.d("hour:minute", hour + ":" + minute);
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
		String stu_id = mStuId.getText().toString();
		String name = mName.getText().toString();
		String contact = mContact.getText().toString();
		
		//�ж�ѧ���Ƿ�Ϊ��
		if (!stu_id.isEmpty())	{
			if (stu_id.length() == 11)	{
				if (!name.isEmpty())	{
					if (!contact.isEmpty())	{
						//������д�����ݿ�
						writeDataToDb();
						finish();
						Toast.makeText(AlterEventActivity.this, "�޸ĳɹ���", Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(AlterEventActivity.this, "������д��ϵ��ʽ��", Toast.LENGTH_SHORT).show();
					}
				}
				else	{
					Toast.makeText(AlterEventActivity.this, "��������Ϊ�գ�", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(AlterEventActivity.this, "ѧ�ű���Ϊ11λ��", Toast.LENGTH_SHORT).show();
			}
		}
		else	{
			Toast.makeText(AlterEventActivity.this, "ѧ�Ų���Ϊ��", Toast.LENGTH_LONG).show();
		}
		
	}

	// ������д�����ݿ�
	private void writeDataToDb() {
		// ����FLAG��ֵȷ��EVENT����
		String EVENT = null;
		String EVENT_DATE = null;
		String EVENT_TIME = null;
		String EVENT_PLACE = null;
		if (FLAG == 1) {
			EVENT = "LostEvent";
			EVENT_DATE = "lost_date";
			EVENT_TIME = "lost_time";
			EVENT_PLACE = "lost_place";
		} else if (FLAG == 2) {
			EVENT = "FoundEvent";
			EVENT_DATE = "found_date";
			EVENT_TIME = "found_time";
			EVENT_PLACE = "found_place";
		} else {
			Log.e("Event Flag", "Unexpected FLAG");
		}
		String stu_id = mStuId.getText().toString();
		String name = mName.getText().toString();
		String contact = mContact.getText().toString();
		String event_place = mEventPlace.getText().toString();
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
		String event_date = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day_of_month);
		//��ȡTimePicker�е�ʱ��
		hour = mTimePicker.getCurrentHour();
		minute = mTimePicker.getCurrentMinute();
		String event_time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + "00";
		
		// ��ȡ��д���ݿ�
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// �ж�ѧ���Ƿ�ı�
		if (stu_id.equals(mOldStuId)) {
			// ѧ��δ�ı䣬���������¼�
			db.execSQL("update " + EVENT +" set owner_name = ? where owner_stu_id = ?",
							new String[]{name, stu_id});
			db.execSQL("update " + EVENT +" set owner_contact = ? where owner_stu_id = ?",
					new String[]{contact, stu_id});
			db.execSQL("update " + EVENT +" set " + EVENT_DATE+ "= ? where owner_stu_id = ?",
					new String[]{event_date, stu_id});
			db.execSQL("update " + EVENT +" set " + EVENT_TIME + "= ? where owner_stu_id = ?",
					new String[]{event_time, stu_id});
			db.execSQL("update " + EVENT +" set " + EVENT_PLACE + "= ? where owner_stu_id = ?",
					new String[]{event_place, stu_id});
			db.execSQL("update " + EVENT +" set description = ? where owner_stu_id = ?",
					new String[]{descrption, stu_id});
		} else {
			// ѧ�Ÿı䣬�½��¼�
			db.execSQL("insert into " + EVENT + " ("
					+ "add_date, "
					+ "add_time, "
					+ "owner_stu_id, "
					+ "owner_name, "
					+ "owner_contact, "
					+ EVENT_DATE + ", "
					+ EVENT_TIME + ", "
					+ EVENT_PLACE + ", "
					+ "description, "
					+ "duration, "
					+ "publisher_stu_id, "
					+ "found_flag, "
					+ "close_flag)"
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					new String[] {add_date, add_time, stu_id, name, contact, event_date, event_time, 
							event_place, descrption, "30", mPublisher, "0", "0"});
			// ɾ����ѧ�ŵ��¼�
			db.execSQL("delete from " + EVENT +" where owner_stu_id = ?",
					new String[] {mOldStuId});
		}
	}
	
}
