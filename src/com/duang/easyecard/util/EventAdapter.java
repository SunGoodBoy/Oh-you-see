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

	//构造函数，初始化
	public EventAdapter(Context context, List<Event> eventList)	{
		this.context = context;
		this.eventList = eventList;
		layoutInflater = LayoutInflater.from(this.context);
	}
	
	//获得长度，一般返回数据的长度即可
	@Override
	public int getCount()	{
		return eventList.size();
	}
	
	//获取当前的实例，看来getItem是BaseAdapter的函数
	@Override
	public Object getItem(int position)	{
		return eventList.get(position);
	}
	
	@Override
	public long getItemId(int position)	{
		return position;
	}
	
	/**
	 * 最重要的方法，每一个item生成的时候，都会执行这个方法，在这个方法中实现数据与item中每个控件的绑定
	*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent)	{
		
		/** convertView对象就是item的界面对象，
		  *  只有为空的时候我们才需要重新赋值一次，这样可以提高效率，
		  *  如果有这个对象的话，系统会自动复用
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
