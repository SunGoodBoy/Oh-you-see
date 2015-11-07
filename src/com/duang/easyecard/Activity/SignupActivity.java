package com.duang.easyecard.Activity;

import com.duang.easyecard.R;
import com.duang.easyecard.R.id;
import com.duang.easyecard.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends Activity{
	
	private EditText mNickname;
	private EditText mStu_id;
	private EditText mPassword;
	private EditText mPassword_confirm;
	private EditText mGrade;
	private EditText mCollege;
	private EditText mEmail;
	private EditText mContact;
	private Button mSignup_button;
	private Button mCancel_signup_button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
	}
}
