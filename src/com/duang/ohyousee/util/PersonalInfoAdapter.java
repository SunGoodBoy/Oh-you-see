package com.duang.ohyousee.util;

import java.io.File;
import java.util.List;
import com.duang.easyecard.R;
import com.duang.ohyousee.Activity.ViewBigUserpicActivity;
import com.duang.ohyousee.model.PersonalInfo;
import com.duang.ohyousee.model.User;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	private final int SCALE = 5;
	
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
				/*
				 *  设置img_content
				 */
				String userpicPath = Environment.getExternalStorageDirectory() + "/EasyEcard";
				String userpicName = User.getCurrentUserStuId().toString() + ".jpg";
				// 判断文件路径是否存在
				File userpicDir = new File(userpicPath);
				if (!userpicDir.exists()) {
					userpicDir.mkdir();
				}
				// 根据头像文件是否存在来决定显示的内容
				File userpic = new File(userpicDir, userpicName);
				try {
					if (userpic.exists()) {
						// 头像文件存在，通过Bitmap显示到ImageView，否则显示系统默认头像
						Bitmap bitmap = BitmapFactory.decodeFile(userpicPath + "/" + userpicName);
						Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
						// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
						bitmap.recycle();
						// 将处理过的图片显示在界面上
						holderImg.img_content.setImageBitmap(newBitmap);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 设置userpic的点击事件
				holderImg.img_content.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// 显示大图
						Intent intent = new Intent(mContext, ViewBigUserpicActivity.class);
						mContext.startActivity(intent);
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
			// Log.d("baseAdapter", "Adapter_:"+(convertView == null));
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
		// Log.d("TYPE:", type + "");
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
