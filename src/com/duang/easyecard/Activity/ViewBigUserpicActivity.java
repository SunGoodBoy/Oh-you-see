package com.duang.easyecard.Activity;

import java.io.File;

import com.duang.easyecard.R;
import com.duang.easyecard.model.User;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;

public class ViewBigUserpicActivity extends BaseActivity {
	
	private ImageView bigImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_big_userpic);
		
		// 实例化控件
		bigImageView = (ImageView) findViewById(R.id.view_big_userpic_imgv);
		// 动态设置bigImageView的高度,使其和宽度相等
		LayoutParams params;
		params = bigImageView.getLayoutParams();
		params.height = params.width;
		// Log.d("params.width", params.width + "");
		bigImageView.setLayoutParams(params);
		
		// 设置img_content
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
				// 将处理过的图片显示在界面上
				bigImageView.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 点击屏幕后销毁活动
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}
}
