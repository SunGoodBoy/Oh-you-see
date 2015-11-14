package com.duang.easyecard.util;

import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.model.Event;
import android.content.Context;

public class EventAdapter extends CommonAdapter<Event>
{
	//���캯������ʼ��
	public EventAdapter(Context context, List<Event> datas, int itemLayoutId)	
	{
		super(context, datas, itemLayoutId);
	}
	

	public void convert(ViewHolder holder, Event event)
	{
		holder.setText(R.id.list_item_text_stu_id, "ѧ��")
			  .setText(R.id.list_item_owner_stu_id, event.getEvent_owner().getStu_id())
			  .setText(R.id.list_item_text_name, "����")
			  .setText(R.id.list_item_owner_name, event.getEvent_owner().getUsername())
			  .setImageResource(R.id.list_user_img, event.getEvent_owner().getImageId());
	}

}