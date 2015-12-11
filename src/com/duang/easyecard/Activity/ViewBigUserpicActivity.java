package com.duang.easyecard.Activity;

import com.duang.easyecard.R;

import android.os.Bundle;
import android.widget.ImageView;

public class ViewBigUserpicActivity extends BaseActivity {
	
	private ImageView bigImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_big_userpic);
		// 实例化控件
		bigImageView = (ImageView) findViewById(R.id.view_big_userpic_imgv);
		
		
	}
	
	
}
