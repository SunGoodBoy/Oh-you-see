package com.duang.easyecard.util;

import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.model.PersonalInfo;

import android.content.Context;

public class PersonalInfoTextAdapter extends CommonAdapter<PersonalInfo>
{
	//构造函数，初始化
		public PersonalInfoTextAdapter(Context context, List<PersonalInfo> datas, int itemLayoutId)	
		{
			super(context, datas, itemLayoutId);
		}
		

		public void convert(ViewHolder holder, PersonalInfo info)
		{
			holder.setText(R.id.personal_info_list_item_title, info.getTitle())
				  .setText(R.id.personal_info_list_item_content, info.getContent());
		}
}
