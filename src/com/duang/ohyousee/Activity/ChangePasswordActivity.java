package com.duang.ohyousee.Activity;
import com.duang.ohyousee.R;
import com.duang.ohyousee.db.MyDatabaseHelper;
import com.duang.ohyousee.model.User;

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
	
	//EditText的焦点监听事件
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId())
		{
		// oldPassword
		case R.id.change_password_old_password_edit:
			if (hasFocus) {
				//获取焦点
				if (oldPasswordEdit.getText().toString().isEmpty()) {
					setConfirmNewPasswordCheckBoxChecked(false);
					hintTextView.setText("提示：请输入旧密码");
				} else if (!checkOldPassword(oldPasswordEdit.getText().toString())){
					setConfirmNewPasswordCheckBoxChecked(false);
					hintTextView.setText("提示：旧密码输入有误，请重新输入");
				}
			} else {
				//失去焦点
				if (oldPasswordEdit.getText().toString().isEmpty()) {
					setConfirmNewPasswordCheckBoxChecked(false);
					hintTextView.setText("提示：请输入旧密码");
				} else {
					if (checkOldPassword(oldPasswordEdit.getText().toString())) {
						setOldPasswordCheckBoxChecked(true);
						hintTextView.setText("提示：请输入新密码");
					} else {
						setConfirmNewPasswordCheckBoxChecked(false);
						hintTextView.setText("提示：旧密码输入有误，请重新输入");
					}
				}
			}
			break;
		
		// newPassword
		case R.id.change_password_new_password_edit:
			if (hasFocus) {
				//获取焦点
				if (oldPasswordEdit.getText().toString().isEmpty()) {
					setNewPasswordCheckBoxChecked(false);
					hintTextView.setText("提示：请输入旧密码");
				} else {
					//旧密码已经通过验证,获取焦点时编辑框内无内容
					if (oldPasswordCheckBox.isChecked()) {
						if (newPasswordEdit.getText().toString().isEmpty()) {
							setNewPasswordCheckBoxChecked(false);
							hintTextView.setText("提示：请输入新密码");
						} else {
							//旧密码已经通过验证，且获取焦点时编辑框内已经有内容
							if (checkNewPassword(newPasswordEdit.getText().toString())) {
								setNewPasswordCheckBoxChecked(true);
							}
						}
					}
				}
			} else {
				//失去焦点
				if (oldPasswordCheckBox.isChecked()) {
					if (!newPasswordEdit.getText().toString().isEmpty()) {
						if (checkNewPassword(newPasswordEdit.getText().toString())) {
							setNewPasswordCheckBoxChecked(true);
							hintTextView.setText("提示：再次输入新密码以确认");
						}
					}
				}
			}
			
		//confirmNewPassword
		case R.id.change_password_confirm_new_password_edit:
			if (hasFocus) {
				//获取焦点
				if (!newPasswordCheckBox.isChecked()) {
					setConfirmNewPasswordCheckBoxChecked(false);
				} else {
					//新密码已经通过验证，且获取焦点时确认密码编辑栏有内容
					if (!confirmNewPasswordEdit.getText().toString().isEmpty()) {
						if (!checkConfirmNewPassword(confirmNewPasswordEdit.getText().toString())) {
							setConfirmNewPasswordCheckBoxChecked(false);
						}
					}
				}
			} else {
				//失去焦点
				if (!newPasswordCheckBox.isChecked()) {
					setConfirmNewPasswordCheckBoxChecked(false);
				} else {
					//新密码已经通过验证，且失去焦点时确认密码编辑栏有内容
					if (!confirmNewPasswordEdit.getText().toString().isEmpty()) {
						if (checkConfirmNewPassword(confirmNewPasswordEdit.getText().toString())) {
							setConfirmNewPasswordCheckBoxChecked(true);
						}
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

	//验证新密码是否符合条件(已经确保不会为空)
	private boolean checkNewPassword(String newPassword) {
		//是否与旧密码相同
		if (!newPassword.equals(oldPasswordEdit.getText().toString())) {
			//其他限制条件
			return true;
		} else {
			setNewPasswordCheckBoxChecked(false);
			hintTextView.setText("提示：新密码不能与旧密码相同");
			return false;
		}
	}

	//验证两次密码输入是否一致
	private boolean checkConfirmNewPassword(String confirmNewPassword) {
		if(confirmNewPassword.equals(newPasswordEdit.getText().toString())) {
			hintTextView.setText("提示：通过验证，点击确定完成修改");
			return true;
		} else {
			hintTextView.setText("提示：新密码两次输入不一致");
			return false;
		}
	}	
	
	//Button的点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		// 取消按钮
		case R.id.change_password_cancel_button:
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
				finish();
				break;
			} else {
				//点击确定按钮并不会使EditText立即失去焦点
				if (!newPasswordCheckBox.isChecked()) {
					setConfirmNewPasswordCheckBoxChecked(false);
				} else {
					//新密码已经通过验证，且点击确认按钮时确认密码编辑栏有内容
					if (!confirmNewPasswordEdit.getText().toString().isEmpty()) {
						if (checkConfirmNewPassword(confirmNewPasswordEdit.getText().toString())) {
							setConfirmNewPasswordCheckBoxChecked(true);
						}
					}
				}
			}
		default:
			break;
		}
	}
	
	//设置oldPasswordCheckBox的选中状态
	protected void setOldPasswordCheckBoxChecked(boolean flag) {
		if (flag) {
			oldPasswordCheckBox.setChecked(true);
		} else {
			oldPasswordCheckBox.setChecked(false);
			newPasswordCheckBox.setChecked(false);
			confirmNewPasswordCheckBox.setChecked(false);
		}
	}
	//设置newPasswordCheckBox的选中状态
	protected void setNewPasswordCheckBoxChecked(boolean flag) {
		if (flag) {
			if (oldPasswordCheckBox.isChecked()) {
				newPasswordCheckBox.setChecked(true);
			}
		} else {
			newPasswordCheckBox.setChecked(false);
			confirmNewPasswordCheckBox.setChecked(false);
		}
	}
	//设置confirmNewPasswordCheckBox的选中状态
	protected void setConfirmNewPasswordCheckBoxChecked(boolean flag) {
		if (flag) {
			if (oldPasswordCheckBox.isChecked()) {
				if (newPasswordCheckBox.isChecked()) {
					confirmNewPasswordCheckBox.setChecked(true);
				}
			}
		} else {
			confirmNewPasswordCheckBox.setChecked(false);
		}
	}
}
