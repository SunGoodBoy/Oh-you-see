package com.duang.ohyousee.Activity;

import com.duang.ohyousee.R;
import com.duang.ohyousee.db.MyDatabaseHelper;
import com.duang.ohyousee.model.User;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SignupActivity extends BaseActivity {
	
	private MyDatabaseHelper dbHelper;
	
	private EditText mStu_id;
	private EditText mPassword;
	private EditText mPassword_confirm;
	private EditText mName;
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
		setContentView(R.layout.signup);
		
		//初始化控件对应的变量
		mStu_id = (EditText) findViewById(R.id.signup_stu_id_edit);
		mPassword = (EditText) findViewById(R.id.signup_password_edit);
		mPassword_confirm = (EditText) findViewById(R.id.signup_password_confirm_edit);
		mName = (EditText) findViewById(R.id.signup_name_edit);
		mRealname = (EditText) findViewById(R.id.signup_realname_edit);
		mGrade = (EditText) findViewById(R.id.signup_grade_edit);
		mCollege = (EditText) findViewById(R.id.signup_college_edit);
		mEmail = (EditText) findViewById(R.id.signup_email_edit);
		mContact = (EditText) findViewById(R.id.signup_contact_edit);
		mRadioMale = (RadioButton) findViewById(R.id.signup_radioMale);
		mSignup_button = (Button) findViewById(R.id.signup_button);
		mCancel_signup_button = (Button) findViewById(R.id.signup_cancel);

		//创建数据库
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
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
								
								//设置为当前用户
								User.setCurrentUserStuId(stu_id);
								
								//通过AlertDialog询问是否直接登录
								AlertDialog.Builder dialog = new AlertDialog.Builder(SignupActivity.this);
								dialog.setTitle("注册成功！");
								dialog.setMessage("您已经成功注册，是否直接登录？");
								dialog.setCancelable(false);
								dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(SignupActivity.this, MainActivity.class);
										startActivity(intent);
										finish();
									}
								});
								dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										//销毁SignupActivity，跳转至登录界面
										finish();
									}
								});
								dialog.show();
								
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
		Cursor cursor = db.query("UserInfo", null, null, null, null, null, null);
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
		String name = mName.getText().toString();
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
		values.put("name", name);
		values.put("real_name", realName);
		values.put("grade", grade);
		values.put("college", college);
		values.put("email", email);
		values.put("contact", contact);
		values.put("gender", gender);
		db.insert("UserInfo", null, values);
		values.clear();
	}
}
