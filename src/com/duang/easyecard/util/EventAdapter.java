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

public class EventAdapter extends CommonAdapter<Event>
{

	//构造函数，初始化
	public EventAdapter(Context context, List<Event> datas)	
	{
		super(context, datas);
	}
	

	public void convert(ViewHolder holder, Event event)
	{
		holder.setText(R.id.list_item_text_stu_id, "学号")
			  .setText(R.id.list_item_owner_stu_id, event.getEvent_owner().getStu_id())
			  .setText(R.id.list_item_text_name, "姓名")
			  .setText(R.id.list_item_owner_name, event.getEvent_owner().getUsername());
	}

}
