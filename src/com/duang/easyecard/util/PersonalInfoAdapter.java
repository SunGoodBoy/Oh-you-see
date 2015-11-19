package com.duang.easyecard.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.Activity.PersonalInfoActivity;
import com.duang.easyecard.model.PersonalInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalInfoAdapter extends CommonAdapter<PersonalInfo>
{
	protected List<PersonalInfo> mDatas;
	protected Context mContext;
	protected LayoutInflater mInflater;
	
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	
	//构造函数
	public PersonalInfoAdapter(Context context, List<PersonalInfo> data, int itemLayoutId)
	{
		super(context, data, itemLayoutId);
	}
	
	//获得当前需要的View样式
	@Override
	public int getItemViewType(int position)	{
		if (position == 0)	{
			return TYPE_1;
		} else	{
			return TYPE_2;
		}
	}
	
	@Override
	public int getViewTypeCount()	{
		return 2;
	}
	
	
	/*
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
				holder1 = new viewHolder1(, parent, R.layout.personal_info_list_item_image, position);
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
				break;
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
			default:
				break;
			}
		}
		
		//设置资源
		switch (type)
		{
		case TYPE_1:
			holder1.textVeiw.setText("用户头像");
			holder1.imageView.setImageResource(R.drawable.app_icon);
			break;
		case TYPE_2:
			break;
		default:
			break;
		}
		
		return convertView;
	}
*/
	//重写CommonAdapter的 getView方法
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		ViewHolder viewHolder_img = null;
		ViewHolder viewHolder_text = null;
		
		int type = getItemViewType(position);
		switch (type)
		{
		case TYPE_1:
			viewHolder_img = new ViewHolder(mContext, parent, R.layout.personal_info_list_item_image, position);
			viewHolder_img.setText(R.id.personal_info_img_title, getItem(position).getTitle());
			viewHolder_img.setImageResource(R.id.personal_info_img, getItem(position).getImgId());
			Log.d("viewHolder_img at position", String.valueOf(position) + "viewHolder_img设置资源成功");
			return viewHolder_img.getConvertView();
		case TYPE_2:
			viewHolder_text = new ViewHolder(mContext, parent, R.layout.personal_info_list_item_text, position);
			viewHolder_text.setText(R.id.personal_info_list_item_title, getItem(position).getTitle());
			viewHolder_text.setText(R.id.personal_info_list_item_content, getItem(position).getContent());
			Log.d("viewHolder_text at position", String.valueOf(position) + "viewHolder_text设置资源成功");
			return viewHolder_text.getConvertView();
		default:
			return convertView;
		}
	}
	

	@Override
	public void convert(ViewHolder holder, PersonalInfo personalInfo) {
		
	}
	
	
}
