package com.duang.easyecard.Activity;

import com.duang.easyecard.R;
import com.duang.easyecard.R.id;
import com.duang.easyecard.R.layout;
import com.duang.easyecard.db.MyDatabaseHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SignupActivity extends Activity{
	
	private MyDatabaseHelper dbHelper;
	
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
		
		//初始化控件对应的变量
		mStu_id = (EditText) findViewById(R.id.stu_id_edit);
		mPassword = (EditText) findViewById(R.id.password_edit);
		mPassword_confirm = (EditText) findViewById(R.id.password_confirm_edit);
		mNickname = (EditText) findViewById(R.id.nickname_edit);
		mRealname = (EditText) findViewById(R.id.realname_edit);
		mGrade = (EditText) findViewById(R.id.grade_edit);
		mCollege = (EditText) findViewById(R.id.college_edit);
		mEmail = (EditText) findViewById(R.id.email_edit);
		mContact = (EditText) findViewById(R.id.contact_edit);
		mRadioMale = (RadioButton) findViewById(R.id.radioMale);
		mSignup_button = (Button) findViewById(R.id.signup_button);
		mCancel_signup_button = (Button) findViewById(R.id.cancel_signup);

		//创建数据库
		dbHelper = new MyDatabaseHelper(this, "EcardInfo.db", null, 1);
		
		//注册按钮的点击事件
		mSignup_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				signUp_main(v);
			}
		});
		

		//“取消注册”按钮的点击事件
		mCancel_signup_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	protected void signUp_main(View v)	{
		
		String stu_id = mStu_id.getText().toString();
		String password = mPassword.getText().toString();
		String password_confirm = mPassword_confirm.getText().toString();
		
		//判断是否可以注册
		if (!stu_id.isEmpty())	{
			if (stu_id.length() == 11)	{
				if (!wasSigned(stu_id))	{
					if (!password.isEmpty())	{
						if (password.length() <= 16)	{
							if (password.equals(password_confirm))	{
								writeDataToDb();
								Toast.makeText(SignupActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
								//销毁SignupActivity，跳转至登录界面
								finish();
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
				else	{
					Toast.makeText(SignupActivity.this, "该帐号已注册", Toast.LENGTH_SHORT).show();
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

	//判断帐号是否已经注册
	private boolean wasSigned(String stu_id_input) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("BasicInfo", null, null, null, null, null, null);
		if (cursor.moveToFirst())	{
			do	{
				if (stu_id_input.equals(cursor.getString(cursor.getColumnIndex("stu_id"))))	{
					return true;
				}
			}	while (cursor.moveToNext());
		}
		cursor.close();
		return false;
	}

	//将数据写入数据库
	private void writeDataToDb() {
		// TODO Auto-generated method stub
		String stu_id = mStu_id.getText().toString();
		String password = mPassword.getText().toString();
		String nickName = mNickname.getText().toString();
		String realName = mRealname.getText().toString();
		String grade = mGrade.getText().toString();
		String college = mCollege.getText().toString();
		String email = mEmail.getText().toString();
		String contact = mContact.getText().toString();
		String gender = mRadioMale.isChecked() ? "男" : "女";
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("stu_id", stu_id);
		values.put("password", password);
		values.put("nick_name", nickName);
		values.put("real_name", realName);
		values.put("grade", grade);
		values.put("college", college);
		values.put("email", email);
		values.put("contact", contact);
		values.put("gender", gender);
		db.insert("BasicInfo", null, values);
		values.clear();
	}
}
