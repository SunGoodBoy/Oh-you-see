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
		
		//ʵ�����ؼ�
		oldPasswordEdit = (EditText) findViewById(R.id.change_password_old_password_edit);
		newPasswordEdit = (EditText) findViewById(R.id.change_password_new_password_edit);
		confirmNewPasswordEdit = (EditText) findViewById(R.id.change_password_confirm_new_password_edit);
		oldPasswordCheckBox = (CheckBox) findViewById(R.id.change_password_old_password_check);
		newPasswordCheckBox = (CheckBox) findViewById(R.id.change_password_new_password_check);
		hintTextView = (TextView) findViewById(R.id.change_password_hint_text);
		confirmNewPasswordCheckBox = (CheckBox) findViewById(R.id.change_password_confirm_new_password_check);
		cancelButton = (Button) findViewById(R.id.change_password_cancel_button);
		confirmButton = (Button) findViewById(R.id.change_password_confirm_button);
		
		//�򿪻򴴽����ݿ�
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		//����EditText�Ľ���ı�
		oldPasswordEdit.setOnFocusChangeListener(this);
		newPasswordEdit.setOnFocusChangeListener(this);
		confirmNewPasswordEdit.setOnFocusChangeListener(this);
		
		//����Button�ĵ���¼�
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
				hintTextView.setText("��ʾ�������������");
			} else {
				if (checkOldPassword(oldPasswordEdit.getText().toString())) {
					hintTextView.setText("��ʾ��������������");
					oldPasswordCheckBox.setChecked(true);
				} else {
					hintTextView.setText("��ʾ��������������������������");
				}
			}
			break;
		
		// newPassword
		case R.id.change_password_new_password_edit:
			if (hasFocus) {
				if (oldPasswordEdit.getText().toString().isEmpty()) {
					if (!oldPasswordCheckBox.isChecked()) {
						hintTextView.setText("��ʾ���������������");
					}
				} else if (checkOldPassword(oldPasswordEdit.getText().toString())) {
					hintTextView.setText("��ʾ��������������");
					oldPasswordCheckBox.setChecked(true);
				} else {
					hintTextView.setText("��ʾ��������������������������");
				}
			} else {
				if (!oldPasswordCheckBox.isChecked()) {
					hintTextView.setText("��ʾ���������������");
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
					hintTextView.setText("��ʾ���������������");
				} else if (!newPasswordCheckBox.isChecked()) {
					hintTextView.setText("��ʾ����������������ٽ���ȷ��");
				} else {
					if (checkConfirmNewPassword(confirmNewPasswordEdit.getText().toString())) {
						hintTextView.setText("��ʾ����֤�ɹ���ȷ�Ϻ��޸���Ч");
						confirmNewPasswordCheckBox.setChecked(true);
					} else {
						hintTextView.setText("��ʾ�������������벻һ��");
					}
				}
			}
			
		}
	}

	//��֤������
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

	//��֤�������Ƿ��������
	private boolean checkNewPassword(String newPassword) {
		//�Ƿ����������ͬ
		if (!newPassword.isEmpty()) {
			if (newPassword.equals(oldPasswordEdit.getText().toString())) {
				hintTextView.setText("��ʾ�������벻�����������ͬ");
				return false;
			} else {
				//������������
				return true;
			}
		} else {
			hintTextView.setText("��ʾ�����벻��Ϊ��");
			return false;
		}
		
	}

	//��֤�������������Ƿ�һ��
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
		// ȡ����ť
		case R.id.change_password_cancel_button:
			Intent intent = new Intent(this, FourthFragment.class);
			startActivity(intent);
			finish();
			break;
		// ȷ����ť
		case R.id.change_password_confirm_button:
			if (confirmNewPasswordCheckBox.isChecked()) {
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.execSQL("update UserInfo set password = ? where stu_id = ?",
						new String[] { newPasswordEdit.getText().toString(), User.getCurrentUserStuId()});
				db.close();
				Toast.makeText(this, "�޸ĳɹ���", Toast.LENGTH_SHORT).show();
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
