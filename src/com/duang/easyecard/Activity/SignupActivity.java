package com.duang.easyecard.Activity;

import com.duang.easyecard.R;
import com.duang.easyecard.R.id;
import com.duang.easyecard.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SignupActivity extends Activity{
	
	private EditText mStu_id;
	private EditText mPassword;
	private EditText mPassword_confirm;
	private EditText mNickname;
	private EditText mRealname;
	private EditText mGrade;
	private EditText mCollege;
	private EditText mEmail;
	private EditText mContact;
	
	private RadioButton mRadioMale;
	private Button mSignup_button;
	private Button mCancel_signup_button;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
		
		//初始化控件对应的变量
		mStu_id = (EditText) findViewById(R.id.stu_id_edit);
		mPassword = (EditText) findViewById(R.id.password_edit);
		mPassword_confirm = (EditText) findViewById(R.id.password_confirm_edit);
		//mNickname = (EditText) findViewById(R.id.nickname_edit);
		//mRealname = (EditText) findViewById(R.id.realname);
		//mGrade = (EditText) findViewById(R.id.grade_edit);
		//mCollege = (EditText) findViewById(R.id.college_edit);
		//mEmail = (EditText) findViewById(R.id.email_edit);
		//mContact = (EditText) findViewById(R.id.contact_edit);
		//mRadioMale = (RadioButton) findViewById(R.id.radioMale);
		mSignup_button = (Button) findViewById(R.id.signup_button);
		mCancel_signup_button = (Button) findViewById(R.id.cancel_signup);

		//注册按钮的点击事件
		mSignup_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				signUp_main(v);
			}
		});
		

		//取消注册按钮的点击事件
		mCancel_signup_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	public void signUp_main(View v)	{
		
		String stu_id = mStu_id.getText().toString();
		String password = mPassword.getText().toString();
		String password_confirm = mPassword_confirm.getText().toString();
		/*String nickName = mNickname.getText().toString();
		String realName = mRealname.getText().toString();
		String grade = mGrade.getText().toString();
		String college = mCollege.getText().toString();
		String email = mEmail.getText().toString();
		String contact = mContact.getText().toString();
		
		String gender = mRadioMale.isChecked() ? "男" : "女";*/
		
		//判断是否可以注册
		if (!stu_id.isEmpty())	{
			if (stu_id.length() == 11)	{
				if (!password.isEmpty())	{
					if (password.length() <= 16)	{
						if (password.equals(password_confirm))	{
							Toast.makeText(SignupActivity.this, "可以写入数据库了", Toast.LENGTH_SHORT).show();
						}
						else	{
							Toast.makeText(SignupActivity.this, "两次密码输入不一致\n请重新输入", Toast.LENGTH_SHORT).show();
						}
					}
					else	{
						Toast.makeText(SignupActivity.this, "密码不能超过16位", Toast.LENGTH_SHORT).show();
					}
				}
				else	{
					Toast.makeText(SignupActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(SignupActivity.this, "学号必须为11位！", Toast.LENGTH_SHORT).show();
			}
		}
		else	{
			Toast.makeText(SignupActivity.this, "学号不能为空", Toast.LENGTH_LONG).show();
		}
		
	}
}
