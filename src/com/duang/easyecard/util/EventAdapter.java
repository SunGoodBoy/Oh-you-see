package com.duang.easyecard.util;

import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.model.Event;
import android.content.Context;

public class EventAdapter extends CommonAdapter<Event>
{
	//构造函数，初始化
	public EventAdapter(Context context, List<Event> datas, int itemLayoutId)	
	{
		super(context, datas, itemLayoutId);
	}
	
	//设置资源
	public void convert(ViewHolder holder, Event event)
	{
		holder.setText(R.id.list_item_text_stu_id, "学号")
			  .setText(R.id.list_item_owner_stu_id, event.getEvent_owner().getStu_id())
			  .setText(R.id.list_item_text_name, "姓名")
			  .setText(R.id.list_item_owner_name, event.getEvent_owner().getUsername())
			  .setImageResource(R.id.list_user_img, event.getEvent_owner().getImageId());
	}

}
