package com.duang.ohyousee.Activity;

import com.duang.ohyousee.R;
import com.duang.ohyousee.model.User;

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
		
		//实例化Button控件
		mPersonalInfo = (Button) viewFragment.findViewById(R.id.personal_info_button);
		mEventAboutMe = (Button) viewFragment.findViewById(R.id.event_about_me_button);
		mEventByMe = (Button) viewFragment.findViewById(R.id.event_by_me_button);
		mChangePassword = (Button) viewFragment.findViewById(R.id.change_password_button);
		mLogout = (Button) viewFragment.findViewById(R.id.logout_button);
		
		//监听点击事件
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
			//跳转到个人信息界面
			Toast.makeText(this.getActivity(),"个人信息", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this.getActivity(), PersonalInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.event_about_me_button:
			//跳转到与我相关界面
			//Toast.makeText(this.getActivity(),"与我相关", Toast.LENGTH_SHORT).show();
			intent = new Intent(this.getActivity(), EventAboutMeActivity.class);
			startActivity(intent);
			break;
		case R.id.event_by_me_button:
			//跳转到我发布的界面
			//Toast.makeText(this.getActivity(),"我发布的", Toast.LENGTH_SHORT).show();
			intent = new Intent(this.getActivity(), EventByMeActivity.class);
			startActivity(intent);
			break;
		case R.id.change_password_button:
			//跳转到修改密码界面
			//Toast.makeText(this.getActivity(),"修改密码", Toast.LENGTH_SHORT).show();
			intent = new Intent(getActivity(), ChangePasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.logout_button:
			//注销登录
			//Toast.makeText(this.getActivity(),"注销登录", Toast.LENGTH_SHORT).show();
			AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
			dialog.setTitle("提示");
			dialog.setMessage("确定注销帐号吗？");
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//跳转至LoginActivity，并结束所在活动，把当前用户置为默认值null
					User.setCurrentUserStuId(null);
					Intent intent = new Intent(getActivity(), LoginActivity.class);
					startActivity(intent);
					getActivity().finish();
				}
			});
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
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
