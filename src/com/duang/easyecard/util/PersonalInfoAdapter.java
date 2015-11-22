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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
     * ��������Դ��position������Ҫ��ʾ�ĵ�layout��type 
     *  
     * type��ֵ�����0��ʼ 
     *  
     * */
	@Override
	public int getItemViewType(int position) {
		PersonalInfo personalInfo = mData.get(position);
		int type = personalInfo.getType();
		Log.e("TYPE:", type + "");
		return type;
	}
	/** 
     * �������е�layout������ 
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
