package com.duang.easyecard.Activity;

import java.lang.reflect.Field;
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
		
		//ʵ�����ؼ�
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
		
		//��ʾԭ����
		textViewTitle.setText(title);
		editText.setText(content);
		
		//�������水ť
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�� editText ��ȡ�޸ĺ������
				content = editText.getText().toString();
				// ���޸ĺ�����ݷ��ص� PersonalInfoActivity
				Intent intent = new Intent();
				intent.putExtra("content_return", content);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	// ����Back��ť�ĵ��
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)	{
			// ��������Ϣ�Ƿ��޸ģ�ֱ�����ٻ
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		}
		return false;
	}
	
}
