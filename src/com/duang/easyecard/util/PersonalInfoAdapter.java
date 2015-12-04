package com.duang.easyecard.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import com.duang.easyecard.R;
import com.duang.easyecard.Activity.PersonalInfoActivity;
import com.duang.easyecard.model.PersonalInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalInfoAdapter extends BaseAdapter
{
	private Context mContext;
	private LayoutInflater mInflater;
	private List<PersonalInfo> mData;
	
	final int VIEW_TYPE = 3;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	
	
	public PersonalInfoAdapter(Context context, List<PersonalInfo> data) {
		mContext = context;
		mData = data;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public PersonalInfo getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PersonalInfo personalInfo = mData.get(position);
		int type = getItemViewType(position);
		ViewHolderImg holderImg = null;
		ViewHolderText holderText = null;
		
		if (convertView == null) {
			switch (type)
			{
			case TYPE_1:
				holderImg = new ViewHolderImg();
				convertView = mInflater.inflate(R.layout.personal_info_list_item_image, null);
				holderImg.img_title = (TextView) convertView.findViewById(R.id.personal_info_img_title);
				holderImg.img_content = (ImageView) convertView.findViewById(R.id.personal_info_img);
				holderImg.img_title.setText(personalInfo.getTitle());
				holderImg.img_content.setImageResource(personalInfo.getImgId());
				// 设置userpic的点击事件
				holderImg.img_content.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, "ChangeUserpic", Toast.LENGTH_SHORT).show();
					}
				});
				convertView.setTag(holderImg);
				break;
			case TYPE_2:
				holderText = new ViewHolderText();
				convertView = mInflater.inflate(R.layout.personal_info_list_item_text, null);
				holderText.text_title = (TextView) convertView.findViewById(R.id.personal_info_list_item_title);
				holderText.text_content = (TextView) convertView.findViewById(R.id.personal_info_list_item_content);
				holderText.text_title.setText(personalInfo.getTitle());
				holderText.text_content.setText(personalInfo.getContent());
				convertView.setTag(holderText);
				break;
			default:
				break;
			}
		} else {
			Log.d("baseAdapter", "Adapter_:"+(convertView == null));
			switch (type)
			{
			case TYPE_1:
				holderImg = (ViewHolderImg) convertView.getTag();
				holderImg.img_title.setText(personalInfo.getTitle());
				holderImg.img_content.setImageResource(personalInfo.getImgId());
				break;
			case TYPE_2:
				holderText = (ViewHolderText) convertView.getTag();
				holderText.text_title.setText(personalInfo.getTitle());
				holderText.text_content.setText(personalInfo.getContent());
				break;
			default:
				break;
			}
		}
		return convertView;
	}
	
	/** 
     * 根据数据源的position返回需要显示的的layout的type 
     *  
     * type的值必须从0开始 
     *  
     * */
	@Override
	public int getItemViewType(int position) {
		PersonalInfo personalInfo = mData.get(position);
		int type = personalInfo.getType();
		Log.d("TYPE:", type + "");
		return type;
	}
	/** 
     * 返回所有的layout的数量 
     *  
     * */ 
	@Override
	public int getViewTypeCount() {
		return 7;
	}
	
	class ViewHolderImg {
		TextView img_title;
		ImageView img_content;
	}

	
	class ViewHolderText {
		TextView text_title;
		TextView text_content;
	}
}
