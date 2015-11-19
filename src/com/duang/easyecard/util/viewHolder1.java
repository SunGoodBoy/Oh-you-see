package com.duang.easyecard.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class viewHolder1 {

	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	
	public viewHolder1(Context context, ViewGroup parent, int layoutId, int position)
	{
		
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}
	
	public static viewHolder1 get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position)
	{
		if (convertView == null)
		{
			return new viewHolder1(context, parent, layoutId, position);
		}else
		{
			viewHolder1 holder = (viewHolder1) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}
	
	/**
	 * 通过viewId获取控件
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId)
	{
		View view = mViews.get(viewId);
		
		if (view == null)
		{
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}
	
	/**
	 * 设置TextView的值
	 * @param viewId
	 * @param text
	 * @return
	 */
	public viewHolder1 setText(int viewId, String text)
	{
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	public viewHolder1 setImageResource(int viewId, int resId)
	{
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
	}

}
