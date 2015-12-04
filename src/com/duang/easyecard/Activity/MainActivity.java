package com.duang.easyecard.Activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.util.ChangeColorIconWithText;
import com.duang.easyecard.util.PagerAdapter;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.SearchView;
import android.view.View.OnClickListener;

public class MainActivity extends BaseFragmentActivity implements OnClickListener,
OnPageChangeListener{

	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	
	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOverflowButtonAlways();
		getActionBar().setDisplayShowHomeEnabled(false);
		
		initView();
		initData();
		initEvent();
	}

	/**
	 * 初始化所有事件
	 */
	private void initEvent() {
		mViewPager.setOnPageChangeListener(this);
	}

	private void initView() {
		setContentView(R.layout.activity_main);
		mViewPager = (ViewPager) this.findViewById(R.id.view_pager);
		mPagerAdapter = new PagerAdapter(this);
		
		//初始化自定义的下方的图标按钮
		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);
		
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);

		one.setIconAlpha(1.0f);
	}

	private void initData() {
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
					}
				});
		mPagerAdapter.addTab(FirstFragment.class, null);
		mPagerAdapter.addTab(SecondFragment.class, null);
		mPagerAdapter.addTab(ThirdFragment.class, null);
		mPagerAdapter.addTab(FourthFragment.class, null);
		mViewPager.setAdapter(mPagerAdapter);
		
	}

	
	//显示菜单，并设置action_search进入编辑状态后搜索按钮的点击事件
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		
		//获取SearchView对象
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		//MenuItem searchItem = menu.findItem(R.id.action_search);  
	    //SearchView searchView = (SearchView) searchItem.getActionView();

		if (searchView == null)
		{
			Log.e("SearchView", "Fail to get SearchView");
			return true;
		}
		
		//获取搜索服务管理器
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		//所在Activity中的 component name，由此系统可以通过Intent唤起
		ComponentName cn = new ComponentName(this, SearchResultActivity.class);
		/*
		 *  通过搜索管理器，从searchable activity(所在的Activity)中获取相关搜索信息，就是searchable的xml设置。
		 *  如果返回null，表示该activity不存在，或者不是searchable
		 */
		SearchableInfo info = searchManager.getSearchableInfo(cn);
		if (info == null)
		{
			Log.e("SearchableInfo", "Fail to get search info");
		}
		//将searchable activity的搜索信息与searchView关联
		searchView.setSearchableInfo(info);

		return true;
	}
	
	//设置菜单按钮的点击事件
	public boolean onMenuItemSelected(int featureId, MenuItem item)	{
		switch (item.getItemId())	{
		case R.id.action_add_lost_info:
			//跳转到添加丢失信息
			Intent intent = new Intent(MainActivity.this, AddLostEventActivity.class);
			startActivity(intent);
			break;
		case R.id.action_add_found_info:
			//跳转到添加拾获信息
			intent = new Intent(MainActivity.this, AddFoundEventActivity.class);
			startActivity(intent);
			break;
		case R.id.action_feedback:
			//跳转到意见反馈
			break;
		case R.id.action_exit:
			//通过AlertDialog询问是否要退出应用程序
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("提示");
			dialog.setMessage("退出应用？");
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					ActivityCollector.finishAll();
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
		}
		return true;
	}
	
	// 设置ActionBar的按钮在标题栏一直显示
	private void setOverflowButtonAlways()
	{
		try
		{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置menu显示icon
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu)
	{

		if (featureId == Window.FEATURE_ACTION_BAR && menu != null)
		{
			if (menu.getClass().getSimpleName().equals("MenuBuilder"))
			{
				try
				{
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return super.onMenuOpened(featureId, menu);
	}

	
	// 监听Back按钮的点击
	public boolean onKeyDown(int keyCode, KeyEvent event)	{
		if (keyCode == KeyEvent.KEYCODE_BACK)	{
			//通过AlertDialog询问是否要退出应用程序
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("提示");
			dialog.setMessage("退出应用？");
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					ActivityCollector.finishAll();
				}
			});
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			dialog.show();
			
			return false;
		}
		
		return false;
	}
	
	
	//点击事件
	@Override
	public void onClick(View v)
	{
		clickTab(v);

	}

	/**
	 * 监听Tab按钮点击
	 * 
	 * @param v
	 */
	private void clickTab(View v)
	{
		resetOtherTabs();

		switch (v.getId())
		{
		case R.id.id_indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			break;
		}
	}
	
	/**
	 * 重置其他的TabIndicator的颜色
	 */
	private void resetOtherTabs()
	{
		for (int i = 0; i < mTabIndicators.size(); i++)
		{
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}



	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels)
	{
		// Log.e("TAG", "position = " + position + " ,positionOffset =  "
		// + positionOffset);
		if (positionOffset > 0)
		{
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}

	}
	
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

}
