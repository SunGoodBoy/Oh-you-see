package com.duang.easyecard.util;

import java.util.List;
import java.util.zip.Inflater;

import com.duang.easyecard.R;
import com.duang.easyecard.model.PersonalInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalInfoAdapter extends BaseAdapter
{
	protected Context mContext;
	protected LayoutInflater mInflater;
	
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	
	//构造函数，初始化
	public PersonalInfoAdapter(Context context)
	{
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	//获得当前需要的View样式
	@Override
	public int getItemViewType(int position)	{
		if (position == 1)	{
			return TYPE_1;
		} else	{
			return TYPE_2;
		}
	}
	
	@Override
	public int getViewTypeCount()	{
		return 2;
	}
	
	
	
	//通过position的值来决定布局
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder1 holder1 = null;
		viewHolder2 holder2 = null;
		int type = getItemViewType(position);
		
		//如果没有convertView，需要new出各个控件
		if (convertView == null)	{
			Log.e("convertView = ", "NULL");
			
			//按照当前所需的样式，确定new的布局
			switch (type)
			{
			case TYPE_1:
				convertView = mInflater.inflate(R.layout.personal_info_list_item_image, parent, false);
				holder1 = new viewHolder1();
				holder1.imageView = (ImageView) convertView.findViewById(R.id.personal_info_img);
				holder1.textVeiw = (TextView) convertView.findViewById(R.id.personal_info_img_title);
				convertView.setTag(holder1);
				break;
			case TYPE_2:
				convertView = mInflater.inflate(R.layout.personal_info_list_item_text, parent, false);
				holder2 = new viewHolder2();
				holder2.text_title = (TextView) convertView.findViewById(R.id.personal_info_list_item_title);
				holder2.text_content = (TextView) convertView.findViewById(R.id.personal_info_list_item_content);
				convertView.setTag(holder2);
				break;
			default:
			}
		} else	{
			//有convertView，按样式，取得不用布局
			switch (type)
			{
			case TYPE_1:
				holder1 = (viewHolder1) convertView.getTag();
				break;
			case TYPE_2:
				holder2 = (viewHolder2) convertView.getTag();
				break;
			}
		}
		return null;
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
