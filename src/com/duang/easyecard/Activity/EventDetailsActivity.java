package com.duang.easyecard.Activity;

import java.lang.reflect.Field;
import java.util.StringTokenizer;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailsActivity extends BaseActivity implements OnClickListener{
	
	private MyDatabaseHelper dbHelper;
	
	//private ImageView imgvUserImg;
	
	private TextView tvStu_id;
	private TextView tvName;
	private TextView tvContact;
	private TextView tvEventTimeTitle;
	private TextView tvEventTime;
	private TextView tvEventPlaceTitle;
	private TextView tvEventPlace;
	private TextView tvEventDescription;
	private TextView tvEventPublisher;
	private TextView tvEventAddTime;
	private TextView tvEventState;
	
	private Button btnCloseEvent;
	private Button btnFunction;
	
	private int FLAG;   //FLAG���ڱ�ʶ�¼�����  {LostEventΪ1, FoundEventΪ2}
	private String mPublisher; //������
	private String mCloseFlag; //�¼��Ĺرձ�־
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setOverflowButtonAlways();
		setContentView(R.layout.event_details);
		
		//�ؼ��ĳ�ʼ�� Ҫ�ڼ��ز����ļ�֮��
		//imgvUserImg = (ImageView) findViewById(R.id.event_details_img);
		
		tvStu_id = (TextView) findViewById(R.id.event_details_stu_id);
		tvName = (TextView) findViewById(R.id.event_details_name);
		tvContact = (TextView) findViewById(R.id.event_details_contact);
		tvEventTimeTitle = (TextView) findViewById(R.id.event_details_event_time_title);
		tvEventTime = (TextView) findViewById(R.id.event_details_event_time);
		tvEventPlaceTitle = (TextView) findViewById(R.id.event_details_event_place_title);
		tvEventPlace = (TextView) findViewById(R.id.event_details_event_place);
		tvEventDescription = (TextView) findViewById(R.id.event_details_event_description);
		tvEventPublisher = (TextView) findViewById(R.id.event_details_event_publisher);
		tvEventAddTime = (TextView) findViewById(R.id.event_details_event_add_time);
		tvEventState = (TextView) findViewById(R.id.event_details_event_state);
		
		btnCloseEvent = (Button) findViewById(R.id.event_details_close_event_button);
		btnFunction = (Button) findViewById(R.id.event_details_function_button);
		
		//�򿪻򴴽����ݿ�
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		if (dbHelper == null)
		{
			Log.e("dbHelper", "Fail to open database.");
		}		
		
		initViews();
		
	}

	private void initViews() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String data = intent.getStringExtra("extra_data");
		Log.d("extra_data in EventDetailsActivity", data);
		
		String split = "_";
		StringTokenizer token = new StringTokenizer(data, split);
		String stu_id = token.nextToken();
		//Log.d("EventDetailsActivity stu_id in data", stu_id);
		
		FLAG = Integer.parseInt(token.nextToken());
		Log.d("EventDetailsActivity FLAG in data", String.valueOf(FLAG));
		
		tvStu_id.setText(stu_id);	//��ʾѧ��
		
		//�ж�FLAG��ֵ��ѡ��ͬ�ı���в�ѯ
		switch (FLAG)
		{
			case 3:
			case 1:
				getInfoFromLostEvent(stu_id);
				//��btnFunction��ʾΪ�����ҵ��ˡ�
				btnFunction.setText("���ҵ���");
				//�����ǰ�û����¼������ߣ������¼�δ���رգ������ر��¼�����ť��Ϊ�ɵ��
				if (User.getCurrentUserStuId().equals(mPublisher) &&
						mCloseFlag.equals("0")) {
					btnCloseEvent.setClickable(true);
					btnCloseEvent.setOnClickListener(this);
				}
				break;
			case 2:
				getInfoFromFoundEvent(stu_id);
				//��btnFunction��ʾΪ������ʧ����
				btnFunction.setText("����ʧ��");
				//�����ǰ�û����¼������ߣ������¼�δ���رգ������ر��¼�����ť��Ϊ�ɵ��
				if (User.getCurrentUserStuId().equals(mPublisher) &&
						mCloseFlag.equals("0")) {
					btnCloseEvent.setClickable(true);
					btnCloseEvent.setOnClickListener(this);
				}
				break;
			default:
		}
		
		btnFunction.setOnClickListener(this);
	}

	//��LostEvent��ȡ��Ϣ����䵽����
	protected void getInfoFromLostEvent(String stu_id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("LostEvent", null, null, null, null, null, null);
		
		if (cursor.moveToLast())
		{
			do{
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))))
				{
					
					//ʧ������
					tvName.setText(cursor.getString(cursor.getColumnIndex("owner_name")));
					//ʧ����ϵ��ʽ
					tvContact.setText(cursor.getString(cursor.getColumnIndex("owner_contact")));
					//��ʾ��ʧʱ�䣬��ʽΪ��2015��11��14��22��54�֡�
					tvEventTimeTitle.setText("��ʧʱ��");
					String date = cursor.getString(cursor.getColumnIndex("lost_date"));
					String split = "-";
					StringTokenizer token = new StringTokenizer(date, split);
					String year = null, month = null, day = null;
					if (token.hasMoreTokens())
					{
						year = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						month = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						day = token.nextToken();
					}
					String time_in_day = cursor.getString(cursor.getColumnIndex("lost_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					String hour = null, minute = null;
					if (token.hasMoreTokens())
					{
						hour = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						minute = token.nextToken();
					}
					tvEventTime.setText(year + "��" + month + "��" + day + "��" + hour + "��" + minute + "��");
					Log.d("EventLostTime", year + "��" + month + "��" + day + "��" + hour + "��" + minute + "��");
					//��ʾ��ʧ�ص�
					tvEventPlaceTitle.setText("��ʧ�ص�");
					tvEventPlace.setText(cursor.getString(cursor.getColumnIndex("lost_place")));
					//��ʾ����
					tvEventDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
					//��ʾ�����ߣ���ѧ�ţ�
					tvEventPublisher.setText(cursor.getString(cursor.getColumnIndex("publisher_stu_id")));
					//�������߸���mPublisher
					mPublisher = cursor.getString(cursor.getColumnIndex("publisher_stu_id"));
					//��ʾ����ʱ��
					date = cursor.getString(cursor.getColumnIndex("lost_date"));
					split = "-";
					token = new StringTokenizer(date, split);
					year = null;
					month = null;
					day = null;
					if (token.hasMoreTokens())
					{
						year = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						month = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						day = token.nextToken();
					}
					time_in_day = cursor.getString(cursor.getColumnIndex("lost_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					hour = null;
					minute = null;
					if (token.hasMoreTokens())
					{
						hour = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						minute = token.nextToken();
					}
					tvEventAddTime.setText(year + "��" + month + "��" + day + "��" + hour + "��" + minute + "��");
					Log.d("EventAddTime", year + "��" + month + "��" + day + "��" + hour + "��" + minute + "��");
					
					//���¼��Ƿ�رյ�״̬���ݸ�mCloseFlag
					mCloseFlag = cursor.getString(cursor.getColumnIndex("close_flag"));
					Log.d("close_flag", cursor.getString(cursor.getColumnIndex("close_flag")));
					
					//��ʾ�¼�״̬  {"�Ѿ��ҵ�", "����Ѱ��", "�Ѿ��ر�"}
					if (cursor.getString(cursor.getColumnIndex("close_flag")).equals("0")) {
						if (cursor.getString(cursor.getColumnIndex("found_flag")).equals("0"))
						{
							Log.d("found_flag", cursor.getString(cursor.getColumnIndex("found_flag")));
							tvEventState.setText("����Ѱ��");
						}
						else
						{
							Log.d("found_flag", cursor.getString(cursor.getColumnIndex("found_flag")));
							tvEventState.setText("�Ѿ��ҵ�");
						}
					} else {
						tvEventState.setText("�Ѿ��ر�");
					}
					
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
	}
	
	//��FoundEvent��ȡ��Ϣ����䵽����
	private void getInfoFromFoundEvent(String stu_id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("FoundEvent", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("owner_stu_id"))))
				{
					//ʧ������
					tvName.setText(cursor.getString(cursor.getColumnIndex("owner_name")));
					//ʰ������ϵ��ʽ
					tvContact.setText(cursor.getString(cursor.getColumnIndex("owner_contact")));
					//��ʾʰ��ʱ�䣬��ʽΪ��2015��11��14��22��54�֡�
					tvEventTimeTitle.setText("ʰ��ʱ��");
					String date = cursor.getString(cursor.getColumnIndex("found_date"));
					String split = "-";
					StringTokenizer token = new StringTokenizer(date, split);
					String year = null, month = null, day = null;
					if (token.hasMoreTokens())
					{
						year = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						month = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						day = token.nextToken();
					}

					String time_in_day = cursor.getString(cursor.getColumnIndex("found_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					String hour = null;
					String minute = null;
					if (token.hasMoreTokens())
					{
						hour = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						minute = token.nextToken();
					}
					tvEventTime.setText(year + "��" + month + "��" + day + "��" + hour + "��" + minute + "��");
					Log.d("EventFoundTime", year + "��" + month + "��" + day + "��" + hour + "��" + minute + "��");
					
					//��ʾʰ��ص�
					tvEventPlaceTitle.setText("ʰ��ص�");
					tvEventPlace.setText(cursor.getString(cursor.getColumnIndex("found_place")));
					//��ʾ����
					tvEventDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
					//��ʾ�����ߣ���ѧ�ţ�
					tvEventPublisher.setText(cursor.getString(cursor.getColumnIndex("publisher_stu_id")));
					//�������߸���mPublisher
					mPublisher = cursor.getString(cursor.getColumnIndex("publisher_stu_id"));
					//��ʾ����ʱ��
					date = cursor.getString(cursor.getColumnIndex("add_date"));
					split = "-";
					token = new StringTokenizer(date, split);
					if (token.hasMoreTokens())
					{
						year = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						month = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						day = token.nextToken();
					}
					time_in_day = cursor.getString(cursor.getColumnIndex("add_time"));
					split = ":";
					token = new StringTokenizer(time_in_day, split);
					hour = null;
					minute = null;
					if (token.hasMoreTokens())
					{
						hour = token.nextToken();
					}
					if (token.hasMoreTokens())
					{
						minute = token.nextToken();
					}
					tvEventAddTime.setText(year + "��" + month + "��" + day + "��" + hour + "��" + minute + "��");
					Log.d("EventAddTime", year + "��" + month + "��" + day + "��" + hour + "��" + minute + "��");
					
					//���¼��Ƿ�رյ�״̬���ݸ�mCloseFlag
					mCloseFlag = cursor.getString(cursor.getColumnIndex("close_flag"));
					Log.d("close_flag", cursor.getString(cursor.getColumnIndex("close_flag")));
					
					//��ʾ�¼�״̬  {"�Ѿ��黹", "Ѱ��ʧ��", "�Ѿ��ر�"}
					if (cursor.getString(cursor.getColumnIndex("close_flag")).equals("0")) {
						if (cursor.getString(cursor.getColumnIndex("returned_flag")).equals("0"))
						{
							Log.d("returned_flag", cursor.getString(cursor.getColumnIndex("returned_flag")));
							tvEventState.setText("Ѱ��ʧ��");
						}
						else
						{
							Log.d("returned_flag", cursor.getString(cursor.getColumnIndex("returned_flag")));
							tvEventState.setText("�Ѿ��黹");
						}
					} else {
						tvEventState.setText("�Ѿ��ر�");
					}
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
	}
	
	// �ر�LostEvent
	private void closeLostEvent() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("update LostEvent set close_flag = ? where publisher_stu_id = ?",
				new String[] { "1", mPublisher});
		db.close();
	}
	// �ر�FoundEvent
	private void closeFoundEvent() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("update FoundEvent set close_flag = ? where publisher_stu_id = ?",
				new String[] { "1", mPublisher});
		db.close();
	}
	//����Button�ĵ���¼�
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		// �������ر��¼�����ť
		case R.id.event_details_close_event_button:
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("��ʾ");
			dialog.setMessage("ȷ���رո��¼���");
			dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// �����¼����ͣ����Ķ�д�����ݿ�
					if (FLAG == 1) {
						closeLostEvent();
						mCloseFlag = "1";
					} else {
						closeFoundEvent();
						mCloseFlag = "1";
					}
				}
			});
			dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			dialog.show();
			break;
		
		case R.id.event_details_function_button:
			Toast.makeText(this, "�Ǿ;���ȡ����ϵ�ɣ�^_^nn-", Toast.LENGTH_SHORT);
			break;
			default:
		}
	}
	
	// �����˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();  
        inflater.inflate(R.menu.menu_event_details, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	// ����ActionBar�İ�ť�ڱ�����һֱ��ʾ
	private void setOverflowButtonAlways()
	{
		try
		{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	// ���ò˵���ť�ĵ���¼�
	public boolean onMenuItemSelected(int featureId, MenuItem item)	{
		switch (item.getItemId())	{
		case R.id.action_alter_event:
			if (mPublisher.equals(User.getCurrentUserStuId())) {
				if (mCloseFlag.equals("0")) {
					Intent intent = new Intent(EventDetailsActivity.this, AlterEventActivity.class);
					intent.putExtra("stu_id__FLAG", tvStu_id.getText().toString() + "__" + FLAG);
					startActivity(intent);
					finish();  // ���ٱ�����Է������¼���
				} else {
					Toast.makeText(this, "�޷��޸��ѹر��¼�", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "ֻ���޸����Լ��������¼�", Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}
	
}
