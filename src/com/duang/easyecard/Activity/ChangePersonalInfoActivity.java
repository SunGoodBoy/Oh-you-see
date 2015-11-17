package com.duang.easyecard.Activity;

import com.duang.easyecard.R;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePersonalInfoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_personal_info);
		
		//ÊµÀý»¯¿Ø¼þ
		EditText edit = (EditText) findViewById(R.id.change_personal_info_edit);
		TextView text = (TextView) findViewById(R.id.change_personal_info_text);
	}
}
