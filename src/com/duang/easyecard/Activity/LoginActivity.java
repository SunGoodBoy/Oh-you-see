package com.duang.easyecard.Activity;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.User;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	
	private MyDatabaseHelper dbHelper;
	
	private Button signin_button;
	private Button login_signup_button;
	private EditText mUsername;
	private EditText mPassword;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        
        signin_button = (Button) findViewById(R.id.signin_button);
        login_signup_button = (Button) findViewById(R.id.login_signup_button);
        mUsername = (EditText) findViewById(R.id.username_edit);
    	mPassword = (EditText) findViewById(R.id.password_edit); 
    	
    	//创建或打开数据库
    	dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
    	
    	//登录按钮的点击事件
    	signin_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login_main(v);
			}
		});
    	
    	//注册按钮的点击事件
    	login_signup_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
				startActivity(intent);
			}
		});
	}
	
	//登录判断
	public void login_main(View v)	{
		String username = mUsername.getText().toString();
		String password = mPassword.getText().toString();
		
		
		if (!username.isEmpty())	{
			if (wasSigned(username))	{
				if (!password.isEmpty())	{
					if (passwordIsRight(username, password))	{
						//跳转到程序功能界面，结束LoginActivity
						
						//设置为当前用户
						User.setCurrentUserStuId(username);
						
						Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
						
					}	else	{
						Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
						mPassword.setText("");
					}
				}	else	{
					Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
				}
				
			}	else	{
				Toast.makeText(LoginActivity.this, "未注册的帐号", Toast.LENGTH_SHORT).show();
			}
		}	else	{
			Toast.makeText(LoginActivity.this, "帐号不能为空", Toast.LENGTH_SHORT).show();
		}
	}
	
	//判断密码是否正确
	protected boolean passwordIsRight(String username, String password)	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("UserInfo", null, null, null, null, null, null);
		if (cursor.moveToFirst())	{
			do {
				if (username.equals(cursor.getString(cursor.getColumnIndex("stu_id"))))	{
					if (password.equals(cursor.getString(cursor.getColumnIndex("password"))))	{
						return true;
					} else
						return false;
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		Log.e("Error in LoginActivity", "Can't match in database.");
		return false;
    }
	
	//判断帐号是否已经注册，true为已经注册，false为未注册
	protected boolean wasSigned(String stu_id_input) {
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
		db.close();
		return false;
	}
    
}