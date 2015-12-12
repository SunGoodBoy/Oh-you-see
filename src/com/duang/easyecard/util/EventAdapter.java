package com.duang.easyecard.util;

import java.io.File;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.model.Event;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class EventAdapter extends CommonAdapter<Event>
{
	// 图片缩小倍数
	private int SCALE = 5;
	
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
			  // 显示默认头像default_userpic
			  .setImageResource(R.id.list_user_img, event.getEvent_owner().getImageId());
		
		/*
		 *  如果event_owner已经设置了头像，显示event_owner的头像
		 */
		String userpicPath = Environment.getExternalStorageDirectory() + "/EasyEcard";
		String userpicName = event.getEvent_owner().getStu_id() + ".jpg";
		// Log.d("EventAdapter", userpicPath);
		// Log.d("EventAdapter", userpicName);
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
				// 将处理过的图片通过控件显示
				holder.setImageBitmap(R.id.list_user_img, newBitmap);
				// Log.d("EventAdapter", "holder.setImageBitmap");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
