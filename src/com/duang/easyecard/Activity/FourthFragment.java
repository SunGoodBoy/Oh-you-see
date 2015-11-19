package com.duang.easyecard.Activity;

import com.duang.easyecard.R;
import com.duang.easyecard.model.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FourthFragment extends Fragment implements OnClickListener{

	private View viewFragment;
	
	private Button mPersonalInfo;
	private Button mEventAboutMe;
	private Button mEventByMe;
	private Button mChangePassword;
	private Button mLogout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewFragment = inflater.inflate(R.layout.fourth, null);
		
		//ʵ����Button�ؼ�
		mPersonalInfo = (Button) viewFragment.findViewById(R.id.personal_info_button);
		mEventAboutMe = (Button) viewFragment.findViewById(R.id.event_about_me_button);
		mEventByMe = (Button) viewFragment.findViewById(R.id.event_by_me_button);
		mChangePassword = (Button) viewFragment.findViewById(R.id.change_password_button);
		mLogout = (Button) viewFragment.findViewById(R.id.logout_button);
		
		//��������¼�
		mPersonalInfo.setOnClickListener(this);
		mEventAboutMe.setOnClickListener(this);
		mEventByMe.setOnClickListener(this);
		mChangePassword.setOnClickListener(this);
		mLogout.setOnClickListener(this);
		
		return viewFragment;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.personal_info_button:
			//��ת��������Ϣ����
			Toast.makeText(this.getActivity(),"������Ϣ", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this.getActivity(), PersonalInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.event_about_me_button:
			//��ת��������ؽ���
			Toast.makeText(this.getActivity(),"�������", Toast.LENGTH_SHORT).show();
			break;
		case R.id.event_by_me_button:
			//��ת���ҷ����Ľ���
			Toast.makeText(this.getActivity(),"�ҷ�����", Toast.LENGTH_SHORT).show();
			break;
		case R.id.change_password_button:
			//��ת���޸��������
			Toast.makeText(this.getActivity(),"�޸�����", Toast.LENGTH_SHORT).show();
			break;
		case R.id.logout_button:
			//ע����¼
			//Toast.makeText(this.getActivity(),"ע����¼", Toast.LENGTH_SHORT).show();
			AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
			dialog.setTitle("��ʾ");
			dialog.setMessage("ȷ��ע���ʺ���");
			dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//��ת��LoginActivity�����������ڻ���ѵ�ǰ�û���ΪĬ��ֵnull
					User.setCurrentUserStuId(null);
					Intent intent = new Intent(getActivity(), LoginActivity.class);
					startActivity(intent);
					getActivity().finish();
				}
			});
			dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			dialog.show();
			break;
		default:
				
		}
	}

}
