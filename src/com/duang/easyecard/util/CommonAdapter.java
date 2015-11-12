package com.duang.easyecard.util;

import java.util.List;

import com.duang.easyecard.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	
	
	public CommonAdapter(Context context, List<T> datas) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, R.layout.list_item, position);

		return holder.getConvertView();
	}
	
	public abstract void convert(ViewHolder holder, T t);

	
}
	
	