package com.duang.ohyousee.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.duang.ohyousee.R;
import com.duang.ohyousee.Activity.NotificationActivity;
import com.duang.ohyousee.Activity.WebViewActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotificationListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	// ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();
 
	public NotificationListViewAdapter(Context context,
		ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		// imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView title;
		TextView publishTime;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.notification_list_view_item, parent, false);
		// Get the position
		resultp = data.get(position);
		
		// Locate the TextView in list_view_item.xml
		title = (TextView) itemView.findViewById(R.id.notification_list_view_item_title);
		publishTime = (TextView) itemView.findViewById(R.id.notification_list_view_item_publish_time);
		
		// Capture position and set results to the TextViews
		title.setText(resultp.get(NotificationActivity.TITLE));
		publishTime.setText(resultp.get(NotificationActivity.PUBLISH_TIME));
		
		// Capture ListView item click
		itemView.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				// Get the position
				resultp = data.get(position);
				Intent intent = new Intent(context, WebViewActivity.class);
				intent.putExtra("postfixUrl", resultp.get(NotificationActivity.POSTFIX_URL));
				context.startActivity(intent);
			}
		});
		return itemView;
	}
}
