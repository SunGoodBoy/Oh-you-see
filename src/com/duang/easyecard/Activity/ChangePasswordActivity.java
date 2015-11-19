package com.duang.easyecard.Activity;
import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.Event;
import com.duang.easyecard.model.User;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePasswordActivity extends BaseActivity implements OnFocusChangeListener, OnClickListener
{
	
	private MyDatabaseHelper dbHelper;
	
	
	
	private EditText oldPasswordEdit;
	private EditText newPasswordEdit;
	private EditText confirmNewPasswordEdit;
	
	private CheckBox oldPasswordCheckBox;
	private CheckBox newPasswordCheckBox;
	private CheckBox confirmNewPasswordCheckBox;
	
	private TextView hintTextView;
	
	private Button cancelButton;
	private Button confirmButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
		
		//实例化控件
		oldPasswordEdit = (EditText) findViewById(R.id.change_password_old_password_edit);
		newPasswordEdit = (EditText) findViewById(R.id.change_password_new_password_edit);
		confirmNewPasswordEdit = (EditText) findViewById(R.id.change_password_confirm_new_password_edit);
		oldPasswordCheckBox = (CheckBox) findViewById(R.id.change_password_old_password_check);
		newPasswordCheckBox = (CheckBox) findViewById(R.id.change_password_new_password_check);
		hintTextView = (TextView) findViewById(R.id.change_password_hint_text);
		confirmNewPasswordCheckBox = (CheckBox) findViewById(R.id.change_password_confirm_new_password_check);
		cancelButton = (Button) findViewById(R.id.change_password_cancel_button);
		confirmButton = (Button) findViewById(R.id.change_password_confirm_button);
		
		//打开或创建数据库
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		//监听EditText的焦点改变
		oldPasswordEdit.setOnFocusChangeListener(this);
		newPasswordEdit.setOnFocusChangeListener(this);
		confirmNewPasswordEdit.setOnFocusChangeListener(this);
		
		//监听Button的点击事件
		cancelButton.setOnClickListener(this);
		confirmButton.setOnClickListener(this);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId())
		{
		// oldPassword
		case R.id.change_password_old_password_edit:
			if (hasFocus) {
				if (oldPasswordEdit.getText().toString().isEmpty()) {
					oldPasswordCheckBox.setChecked(false);
					newPasswordCheckBox.setChecked(false);
					confirmNewPasswordCheckBox.setChecked(false);
				} else if (!checkOldPassword(oldPasswordEdit.getText().toString())){
					oldPasswordCheckBox.setChecked(false);
					newPasswordCheckBox.setChecked(false);
					confirmNewPasswordCheckBox.setChecked(false);
				}
				hintTextView.setText("提示：请输入旧密码");
			} else {
				if (checkOldPassword(oldPasswordEdit.getText().toString())) {
					hintTextView.setText("提示：请输入新密码");
					oldPasswordCheckBox.setChecked(true);
				} else {
					hintTextView.setText("提示：旧密码输入有误，请重新输入");
				}
			}
			break;
		
		// newPassword
		case R.id.change_password_new_password_edit:
			if (hasFocus) {
				if (oldPasswordEdit.getText().toString().isEmpty()) {
					if (!oldPasswordCheckBox.isChecked()) {
						hintTextView.setText("提示：请先输入旧密码");
					}
				} else if (checkOldPassword(oldPasswordEdit.getText().toString())) {
					hintTextView.setText("提示：请输入新密码");
					oldPasswordCheckBox.setChecked(true);
				} else {
					hintTextView.setText("提示：旧密码输入有误，请重新输入");
				}
			} else {
				if (!oldPasswordCheckBox.isChecked()) {
					hintTextView.setText("提示：请先输入旧密码");
				} else {
					if (!newPasswordEdit.getText().toString().isEmpty()) {
						if (checkNewPassword(newPasswordEdit.getText().toString())) {
							newPasswordCheckBox.setChecked(true);
						}
					}
				}
			}
			
		//confirmNewPassword
		case R.id.change_password_confirm_new_password_edit:
			if (hasFocus) {
				if (!oldPasswordCheckBox.isChecked()) {
					hintTextView.setText("提示：请先输入旧密码");
				} else if (!newPasswordCheckBox.isChecked()) {
					hintTextView.setText("提示：请输入新密码后再进行确认");
				} else {
					if (checkConfirmNewPassword(confirmNewPasswordEdit.getText().toString())) {
						hintTextView.setText("提示：验证成功，确认后修改生效");
						confirmNewPasswordCheckBox.setChecked(true);
					} else {
						hintTextView.setText("提示：两次密码输入不一致");
					}
				}
			}
			
		}
	}

	//验证旧密码
	public boolean checkOldPassword(String oldPassword) {
		Log.d("currentUserStuId", User.getCurrentUserStuId());
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("UserInfo", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if (User.getCurrentUserStuId().equals(cursor.getString(cursor.getColumnIndex("stu_id"))))
				{
					if (oldPassword.equals(cursor.getString(cursor.getColumnIndex("password")))) {
						cursor.close();
						db.close();
						return true;
					}
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		Log.e("Error in ChangePasswordActivity", "UserInfo error, can't match data in db.");
		return false;
	}

	//验证新密码是否符合条件
	private boolean checkNewPassword(String newPassword) {
		//是否与旧密码相同
		if (!newPassword.isEmpty()) {
			if (newPassword.equals(oldPasswordEdit.getText().toString())) {
				hintTextView.setText("提示：新密码不能与旧密码相同");
				return false;
			} else {
				//其他限制条件
				return true;
			}
		} else {
			hintTextView.setText("提示：密码不能为空");
			return false;
		}
		
	}

	//验证两次密码输入是否一致
	private boolean checkConfirmNewPassword(String confirmNewPassword) {
		if(confirmNewPassword.equals(newPasswordEdit.getText().toString()) ||
				!confirmNewPassword.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}	
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		// 取消按钮
		case R.id.change_password_cancel_button:
			Intent intent = new Intent(this, FourthFragment.class);
			startActivity(intent);
			finish();
			break;
		// 确定按钮
		case R.id.change_password_confirm_button:
			if (confirmNewPasswordCheckBox.isChecked()) {
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.execSQL("update UserInfo set password = ? where stu_id = ?",
						new String[] { newPasswordEdit.getText().toString(), User.getCurrentUserStuId()});
				db.close();
				Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
				intent = new Intent(this, FourthFragment.class);
				startActivity(intent);
				finish();
				break;
			}
		default:
			break;
		}
	}
	
}
