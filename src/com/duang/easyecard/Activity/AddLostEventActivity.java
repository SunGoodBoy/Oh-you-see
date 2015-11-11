package com.duang.easyecard.Activity;

import com.duang.easyecard.R;

import android.os.Bundle;
import android.view.Window;

public class AddLostEventActivity extends BaseActivity	{

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_lost_event);
		
	}
}
