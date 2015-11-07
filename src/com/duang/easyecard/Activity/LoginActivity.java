package com.duang.easyecard.Activity;

import com.duang.easyecard.R;
import com.duang.easyecard.R.id;
import com.duang.easyecard.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private Button signin_button;
	private Button login_signup_button;
	private EditText usernameEditText;
	private EditText passwordEditText;
	final String usernameStored = "10000";
	final String passwordStored = "123456";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        
        signin_button = (Button) findViewById(R.id.signin_button);
        login_signup_button = (Button) findViewById(R.id.login_signup_button);
        usernameEditText = (EditText) findViewById(R.id.username_edit);
    	passwordEditText = (EditText) findViewById(R.id.password_edit); 
    	
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
		String mUsername = usernameEditText.getText().toString();
		String mPassword = passwordEditText.getText().toString();
		
		if (signInJudge(mUsername, mPassword))	{
			//Intent intent = new Intent();
			//intent.setClass(LoginActivity.this, ****************要跳转的Activity*********************)
			//startActivity(intent);
			this.finish();
		}
	}
	
	//判断帐号密码输入是否正确
	public boolean signInJudge(String stu_id, String password)	{
		
		Log.d("Input", stu_id);
		Log.d("Input", password);
		Log.d("Stored", usernameStored);
		Log.d("Stored", passwordStored);
		
		//判断输入是否为空
		if (stu_id.isEmpty() || password.isEmpty())
		{
			Toast.makeText(LoginActivity.this, "帐号或密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		//比较字符串用equal方法
     	if (stu_id.equals(usernameStored) && password.equals(passwordStored))	{
     		Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
     		return true;
     	}
     	else if (stu_id.equals(usernameStored) && !password.equals(passwordStored))	{
     		Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
     		passwordEditText.setText("");
     		return false;
     	}
     	else if (!stu_id.equals(usernameStored))	{
     		Toast.makeText(LoginActivity.this, "帐号不存在", Toast.LENGTH_SHORT).show();
     		return false;
     	}
     	else
     		return false;
    }
    
}