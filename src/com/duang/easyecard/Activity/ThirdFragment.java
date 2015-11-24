package com.duang.easyecard.Activity;

import com.duang.easyecard.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ThirdFragment extends Fragment implements OnClickListener{

	private View viewFragment;
	
	private Button mWebViewButton;
	private Button mCallPhone;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewFragment=inflater.inflate(R.layout.third, null);
		
		// ʵ�����ؼ�
		mWebViewButton = (Button) viewFragment.findViewById(R.id.campus_open_webview_button);
		mCallPhone = (Button) viewFragment.findViewById(R.id.campus_call_phone_button);
		
		// ����Button�ĵ���¼�
		mWebViewButton.setOnClickListener(this);
		mCallPhone.setOnClickListener(this);
		
		return viewFragment;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.campus_call_phone_button:
			Log.d("ThirdFragment", "ready_to_call_phone");
			AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
			dialog.setTitle("��ʾ");
			dialog.setMessage("��ȷ��Ҫ�����ʧ�绰\n(0532-6678-2221)��");
			dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// ͨ��Intent���ò���绰����
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "053266782221"));
					startActivity(intent);
				}
			});
			dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			dialog.show();
		}
		
			
	}
}
