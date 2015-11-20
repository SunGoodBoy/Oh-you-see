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
	
	//EditText�Ľ�������¼�
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId())
		{
		// oldPassword
		case R.id.change_password_old_password_edit:
			if (hasFocus) {
				//��ȡ����
				if (oldPasswordEdit.getText().toString().isEmpty()) {
					setConfirmNewPasswordCheckBoxChecked(false);
					hintTextView.setText("��ʾ�������������");
				} else if (!checkOldPassword(oldPasswordEdit.getText().toString())){
					setConfirmNewPasswordCheckBoxChecked(false);
					hintTextView.setText("��ʾ��������������������������");
				}
			} else {
				//ʧȥ����
				if (oldPasswordEdit.getText().toString().isEmpty()) {
					setConfirmNewPasswordCheckBoxChecked(false);
					hintTextView.setText("��ʾ�������������");
				} else {
					if (checkOldPassword(oldPasswordEdit.getText().toString())) {
						setOldPasswordCheckBoxChecked(true);
						hintTextView.setText("��ʾ��������������");
					} else {
						setConfirmNewPasswordCheckBoxChecked(false);
						hintTextView.setText("��ʾ��������������������������");
					}
				}
			}
			break;
		
		// newPassword
		case R.id.change_password_new_password_edit:
			if (hasFocus) {
				//��ȡ����
				if (oldPasswordEdit.getText().toString().isEmpty()) {
					setNewPasswordCheckBoxChecked(false);
					hintTextView.setText("��ʾ�������������");
				} else {
					//�������Ѿ�ͨ����֤,��ȡ����ʱ�༭����������
					if (oldPasswordCheckBox.isChecked()) {
						if (newPasswordEdit.getText().toString().isEmpty()) {
							setNewPasswordCheckBoxChecked(false);
							hintTextView.setText("��ʾ��������������");
						} else {
							//�������Ѿ�ͨ����֤���һ�ȡ����ʱ�༭�����Ѿ�������
							if (checkNewPassword(newPasswordEdit.getText().toString())) {
								setNewPasswordCheckBoxChecked(true);
							}
						}
					}
				}
			} else {
				//ʧȥ����
				if (oldPasswordCheckBox.isChecked()) {
					if (!newPasswordEdit.getText().toString().isEmpty()) {
						if (checkNewPassword(newPasswordEdit.getText().toString())) {
							setNewPasswordCheckBoxChecked(true);
							hintTextView.setText("��ʾ���ٴ�������������ȷ��");
						}
					}
				}
			}
			
		//confirmNewPassword
		case R.id.change_password_confirm_new_password_edit:
			if (hasFocus) {
				//��ȡ����
				if (!newPasswordCheckBox.isChecked()) {
					setConfirmNewPasswordCheckBoxChecked(false);
				} else {
					//�������Ѿ�ͨ����֤���һ�ȡ����ʱȷ������༭��������
					if (!confirmNewPasswordEdit.getText().toString().isEmpty()) {
						if (!checkConfirmNewPassword(confirmNewPasswordEdit.getText().toString())) {
							setConfirmNewPasswordCheckBoxChecked(false);
						}
					}
				}
			} else {
				//ʧȥ����
				if (!newPasswordCheckBox.isChecked()) {
					setConfirmNewPasswordCheckBoxChecked(false);
				} else {
					//�������Ѿ�ͨ����֤����ʧȥ����ʱȷ������༭��������
					if (!confirmNewPasswordEdit.getText().toString().isEmpty()) {
						if (checkConfirmNewPassword(confirmNewPasswordEdit.getText().toString())) {
							setConfirmNewPasswordCheckBoxChecked(true);
						}
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

	//��֤�������Ƿ��������(�Ѿ�ȷ������Ϊ��)
	private boolean checkNewPassword(String newPassword) {
		//�Ƿ����������ͬ
		if (!newPassword.equals(oldPasswordEdit.getText().toString())) {
			//������������
			return true;
		} else {
			setNewPasswordCheckBoxChecked(false);
			hintTextView.setText("��ʾ�������벻�����������ͬ");
			return false;
		}
	}

	//��֤�������������Ƿ�һ��
	private boolean checkConfirmNewPassword(String confirmNewPassword) {
		if(confirmNewPassword.equals(newPasswordEdit.getText().toString())) {
			hintTextView.setText("��ʾ��ͨ����֤�����ȷ������޸�");
			return true;
		} else {
			hintTextView.setText("��ʾ���������������벻һ��");
			return false;
		}
	}	
	
	//Button�ĵ���¼�
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
				intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		default:
			break;
		}
	}
	
	//����oldPasswordCheckBox��ѡ��״̬
	protected void setOldPasswordCheckBoxChecked(boolean flag) {
		if (flag) {
			oldPasswordCheckBox.setChecked(true);
		} else {
			oldPasswordCheckBox.setChecked(false);
			newPasswordCheckBox.setChecked(false);
			confirmNewPasswordCheckBox.setChecked(false);
		}
	}
	//����newPasswordCheckBox��ѡ��״̬
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
	//����confirmNewPasswordCheckBox��ѡ��״̬
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
