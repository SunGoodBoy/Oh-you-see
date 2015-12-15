package com.duang.ohyousee.Activity;

import java.util.StringTokenizer;

import com.duang.easyecard.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePersonalInfoActivity extends BaseActivity {
	
	private TextView textViewTitle;
	private EditText editText;
	@SuppressWarnings("unused")
	private TextView textViewHint;
	private Button saveButton;
	
	private String title;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setOverflowButtonAlways();
		setContentView(R.layout.change_personal_info);
		
		//实例化控件
		textViewTitle = (TextView) findViewById(R.id.change_personal_info_title);
		editText = (EditText) findViewById(R.id.change_personal_info_edit);
		textViewHint = (TextView) findViewById(R.id.change_personal_info_text);
		saveButton = (Button) findViewById(R.id.change_personal_info_save_button);
		
		Intent intent = getIntent();
		String data = intent.getStringExtra("title-content");
		String split = "-";
		StringTokenizer token = new StringTokenizer(data, split);
		if (token.hasMoreTokens()) {
			title = token.nextToken();
		}
		if (token.hasMoreTokens()) {
			content = token.nextToken();
		}
		
		//显示原内容
		textViewTitle.setText(title);
		editText.setText(content);
		
		//监听保存按钮
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//从 editText 获取修改后的内容
				content = editText.getText().toString();
				// 将修改后的内容返回到 PersonalInfoActivity
				Intent intent = new Intent();
				intent.putExtra("content_return", content);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	// 监听Back按钮的点击
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)	{
			// 不考虑信息是否修改，直接销毁活动
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		}
		return false;
	}
	
}
