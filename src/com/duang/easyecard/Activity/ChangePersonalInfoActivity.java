package com.duang.easyecard.Activity;

import java.lang.reflect.Field;

import com.duang.easyecard.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePersonalInfoActivity extends BaseActivity {
	
	private TextView textViewTitle;
	private EditText editText;
	private TextView textViewHint;
	private Button saveButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setOverflowButtonAlways();
		setContentView(R.layout.change_personal_info);
		
		//ÊµÀý»¯¿Ø¼þ
		textViewTitle = (TextView) findViewById(R.id.change_personal_info_title);
		editText = (EditText) findViewById(R.id.change_personal_info_edit);
		textViewHint = (TextView) findViewById(R.id.change_personal_info_text);
		saveButton = (Button) findViewById(R.id.change_personal_info_save_button);
	}
	
	
}
