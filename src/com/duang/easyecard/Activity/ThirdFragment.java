package com.duang.easyecard.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duang.easyecard.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ThirdFragment extends Fragment implements OnItemClickListener{

	private View viewFragment;
	
	private GridView gridView;
	private List<Map<String, Object>> data_list;
	private SimpleAdapter sim_adapter;
	
	// ItemImageͼ���װΪһ������
	private int [] iconImage = {
			R.drawable.web_view_icon,
			R.drawable.phone_icon,
			R.drawable.notifications_icon,
			R.drawable.rules_icon,
			R.drawable.download_icon,
			R.drawable.help_icon
	};
	// ItemText��װ����
	private String[] iconText = {"һ��ͨ��վ", "�����ʧ�绰", "֪ͨ",
								 "�����ƶ�",   "�����ļ�",  "У԰������"};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewFragment=inflater.inflate(R.layout.third, null);
		
		// ʵ�����ؼ�
		gridView = (GridView) viewFragment.findViewById(R.id.grid_view);
		
		// �½�List
		data_list = new ArrayList<Map<String, Object>>();
		// ��ȡ����
		getData();
		// �½�������
		String [] from = {"image", "text"};
		int [] to = {R.id.grid_view_item_img, R.id.grid_view_item_text};
		sim_adapter = new SimpleAdapter(this.getActivity(), data_list, R.layout.grid_view_item, from, to);
		// ����������
		gridView.setAdapter(sim_adapter);
		// ���ü�����
		gridView.setOnItemClickListener(this);
		return viewFragment;
	}
	
	public List<Map<String, Object>> getData(){        
        //cion��iconName�ĳ�������ͬ�ģ�������ѡ��һ������
        for(int i = 0; i < iconImage.length; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", iconImage[i]);
            map.put("text", iconText[i]);
            data_list.add(map);
        }
            
        return data_list;
    }

	// Item�ĵ���¼�,����ͼƬID��ȷ���������
	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		switch (iconImage[position]) {
		// ����У԰һ��ͨ��վ
		case R.drawable.web_view_icon:
			Intent intent = new Intent(this.getActivity(), WebViewActivity.class);
			intent.putExtra("postfixUrl", "homeLogin.action");
			startActivity(intent);
			break;
		// �����ʧ�绰
		case R.drawable.phone_icon:
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
			break;
		// ֪ͨ
		case R.drawable.notifications_icon:
			intent = new Intent(this.getActivity(), NotificationActivity.class);
			intent.putExtra("FLAG", "N");
			startActivity(intent);
			break;
		// �����ƶ�
		case R.drawable.rules_icon:
			intent = new Intent(this.getActivity(), NotificationActivity.class);
			intent.putExtra("FLAG", "R");
			startActivity(intent);
			break;
		// �����ļ�
		case R.drawable.download_icon:
			intent = new Intent(this.getActivity(), NotificationActivity.class);
			intent.putExtra("FLAG", "D");
			startActivity(intent);
			break;
		// У԰������
		case R.drawable.help_icon:
			intent = new Intent(this.getActivity(), NotificationActivity.class);
			intent.putExtra("FLAG", "H");
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}

