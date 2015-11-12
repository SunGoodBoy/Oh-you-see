package com.duang.easyecard.util;

import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.model.Event;
import com.duang.easyecard.model.User;

import android.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter{

	private Context context;
	private List<Event> eventList;
	private LayoutInflater layoutInflater;

	//���캯������ʼ��
	public EventAdapter(Context context, List<Event> eventList)	{
		this.context = context;
		this.eventList = eventList;
		layoutInflater = LayoutInflater.from(this.context);
	}
	
	//��ó��ȣ�һ�㷵�����ݵĳ��ȼ���
	@Override
	public int getCount()	{
		return eventList.size();
	}
	
	//��ȡ��ǰ��ʵ��������getItem��BaseAdapter�ĺ���
	@Override
	public Object getItem(int position)	{
		return eventList.get(position);
	}
	
	@Override
	public long getItemId(int position)	{
		return position;
	}
	
	/**
	 * ����Ҫ�ķ�����ÿһ��item���ɵ�ʱ�򣬶���ִ����������������������ʵ��������item��ÿ���ؼ��İ�
	*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent)	{
		
		/** convertView�������item�Ľ������
		  *  ֻ��Ϊ�յ�ʱ�����ǲ���Ҫ���¸�ֵһ�Σ������������Ч�ʣ�
		  *  ������������Ļ���ϵͳ���Զ�����
		  */
		if (convertView == null)	{
			convertView = layoutInflater.inflate(R.layout.list_item, null);
		}
		ImageView userImage = (ImageView) convertView.findViewById(R.id.list_user_img);
		TextView userStu_id = (TextView) convertView.findViewById(R.id.list_user_stu_id);
		TextView userName = (TextView) convertView.findViewById(R.id.list_user_name);
		//TextView userCollege = (TextView) view.findViewById(R.id.list_user_college);
		//TextView userContact = (TextView) view.findViewById(R.id.list_user_contact);
		
		userImage.setBackgroundResource(eventList.get(position).getEvent_owner().getImageId());
		userStu_id.setText(eventList.get(position).getEvent_owner().getStu_id());
		userName.setText(eventList.get(position).getEvent_owner().getUsername());
		
		return convertView;
	}
}
