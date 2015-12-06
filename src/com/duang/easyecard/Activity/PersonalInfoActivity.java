package com.duang.easyecard.Activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.duang.easyecard.R;
import com.duang.easyecard.db.MyDatabaseHelper;
import com.duang.easyecard.model.PersonalInfo;
import com.duang.easyecard.model.User;
import com.duang.easyecard.util.PersonalInfoAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PersonalInfoActivity extends BaseActivity implements  OnItemClickListener
{
	private MyDatabaseHelper dbHelper;

	private ListView listView;
	private PersonalInfoAdapter mAdapter;
	private List<PersonalInfo> personalInfoList = new ArrayList<PersonalInfo>();
	private Handler mHandler;
	
	private static final int TAKE_PICTURE = 10;
	
	private Uri imageUri;
	private int outputX = 500;
	private int outputY = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info);
		
		// 打开或创建数据库
		dbHelper = new MyDatabaseHelper(this, "EasyEcard.db", null, 1);
		
		geneItems(User.getCurrentUserStuId());
		
		initView();
		
	}

	private void geneItems(String stu_id) 
	{
		personalInfoList = new ArrayList<PersonalInfo>();
		
		// 从数据库中取出数据生成项
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("UserInfo", null, null, null, null, null, null);
		if (cursor.moveToLast())
		{
			do{
				if (stu_id.equals(cursor.getString(cursor.getColumnIndex("stu_id"))))
				{
					PersonalInfo personalInfo = new PersonalInfo("用户头像");
					personalInfo.setType(0);
					personalInfo.setTitle("用户头像");
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("学号");
					personalInfo.setType(1);
					personalInfo.setTitle("学号");
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("stu_id")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("姓名");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("name")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("真实姓名");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("real_name")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("性别");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("gender")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("年级");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("grade")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("学院");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("college")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("系别");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("department")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("联系方式");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("contact")));
					personalInfoList.add(personalInfo);
					
					personalInfo = new PersonalInfo("电子邮箱");
					personalInfo.setType(1);
					personalInfo.setContent(cursor.getString(cursor.getColumnIndex("email")));
					personalInfoList.add(personalInfo);
					
					Log.d("In PersonalInfoActivity", "The entity is ready");
					break;
				}
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		mAdapter = new PersonalInfoAdapter(this, personalInfoList);
	}

	private void initView() 
	{
		//Log.d("initView", "start initView");
		listView = (ListView) findViewById(R.id.listView_personal_info);
		mAdapter = new PersonalInfoAdapter(this, personalInfoList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
		mHandler = new Handler();
	}
	
	/**
	 *  根据position的值来决定修改信息对应的Activity
	 *  intent需要携带要修改的内容进行修改
	 *  修改完成后需要携带修改后的信息返回
	 */
	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		
		switch (position)
		{
		case 0:
			// 修改头像，调用对话框选择图片来源
			AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInfoActivity.this);
			builder.setTitle("修改头像");
			builder.setNegativeButton("取消", null);
			builder.setItems(new String[]{"拍照", "从相册中选取"}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					// 拍照
					case 0:
						// 创建File对象，用于存储拍照后的图片
						String saveDir = Environment.getExternalStorageDirectory() + "/EasyEcard";
						String fileName = User.getCurrentUserStuId() + ".jpg";
						File dir = new File(saveDir);
						if (!dir.exists()) {
							dir.mkdir();
						}
						File outputImage = new File(dir, fileName);
						try {
							if (outputImage.exists()) {
								outputImage.delete();
							}
							outputImage.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
						imageUri = Uri.fromFile(outputImage);
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(intent, TAKE_PICTURE);  // 启动相机程序
						break;
					// 从相册中选取
					case 1:
						// 创建File对象，用于存储拍照后的图片
						saveDir = Environment.getExternalStorageDirectory() + "/EasyEcard";
						fileName = User.getCurrentUserStuId() + ".jpg";
						dir = new File(saveDir);
						if (!dir.exists()) {
							dir.mkdir();
						}
						outputImage = new File(dir, fileName);
						try {
							if (outputImage.exists()) {
								outputImage.delete();
							}
							outputImage.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
						imageUri = Uri.fromFile(outputImage);
						intent = new Intent("android.intent.action.GET_CONTENT");
						intent.setType("image/*");
						intent.putExtra("crop", "true");
						intent.putExtra("scale", "true");
				        intent.putExtra("aspectX", 1);
				        intent.putExtra("aspectY", 1);
				        intent.putExtra("outputX", outputX);
				        intent.putExtra("outputY", outputY);
				        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
				        intent.putExtra("return-data", false);
				        intent.putExtra("noFaceDetection", true);  // 取消人脸识别
				        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  // 输出地址
				        startActivity(intent);
				        break;

					default:
						break;
					}
				}
			});
			builder.create().show();
			break;
		case 1:
			// 学号，不允许更改
			break;
		case 4:
			// 通过AlertDialog来让用户选择修改性别
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("更改性别");
			dialog.setMessage("请选择您的性别");
			dialog.setPositiveButton("男", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 将修改后的性别“男”写入数据库
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					db.execSQL("update UserInfo set gender = ? where stu_id = ?",
							new String[]{"男", User.getCurrentUserStuId()});
					db.close();
				}
			});
			dialog.setNegativeButton("女", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 将修改后的性别“女”写入数据库
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					db.execSQL("update UserInfo set gender = ? where stu_id = ?",
							new String[]{"女", User.getCurrentUserStuId()});
					db.close();
				}
			});
			dialog.show();
			refreshPersonalInfoList();	// 刷新listView
			break;
		default:
			// 将对应位置的title和content传给ChangePersonalInfo，position作为请求码
			String title = ((PersonalInfo) view.getItemAtPosition(position)).getTitle();
			String content = ((PersonalInfo) view.getItemAtPosition(position)).getContent();
			Log.d("title-content at position", title + "-" + content + " at " + position);
			Intent intent = new Intent(this, ChangePersonalInfoActivity.class);
			intent.putExtra("title-content", title + "-" + content);
			startActivityForResult(intent, position);
			break;
		}
	}

	// 刷新 personalInfoList
	private void refreshPersonalInfoList() {
		Log.d("In PersonalInfoActivity", "refreshPersonalInfoList");
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				personalInfoList.clear();
				geneItems(User.getCurrentUserStuId());
				mAdapter = new PersonalInfoAdapter(getApplicationContext(), personalInfoList);
				listView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			}
		}, 1000);
	}
	
	// 处理ChangePersonalInfoActivity的返回结果
	@Override
	protected void onActivityResult(int requesCode, int resultCode, Intent data) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String content = data.getStringExtra("content_return");
		switch (requesCode)
		{
		case 2:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set name = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
			}
			break;
		case 3:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set real_name = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
			}
			break;
		case 5:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set grade = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
			}
			break;
		case 6:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set college = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
			}
			break;
		case 7:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set department = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
			}
			break;
		case 8:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set contact = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
			}
			break;
		case 9:
			if (resultCode == RESULT_OK) {
				db.execSQL("update UserInfo set email = ? where stu_id = ?",
						new String[]{content, User.getCurrentUserStuId()});
			}
			break;
		case TAKE_PICTURE:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Log.d("onActivityResult", "TAKE_PICTURE");
					Intent intent = new Intent("com.android.camera.action.CROP");
					intent.setDataAndType(imageUri, "image/*");
					intent.putExtra("crop", "true");
					intent.putExtra("scale", "true");
			        //intent.putExtra("aspectX", 1);
			        //intent.putExtra("aspectY", 1);
			        //intent.putExtra("outputX", outputX);
			        //intent.putExtra("outputY", outputY);
			        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			        intent.putExtra("return-data", false);
			        //intent.putExtra("noFaceDetection", true);  // 取消人脸识别
			        if (imageUri == null) {
			        	Log.e("onActivityResult", "imageUri is null.");
			        }
			        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			        startActivity(intent);  // 启动裁剪程序
				}
			} else {
				Log.e("Result for take photo", "Failed.");
			}
			break;
		default:
			break;
		}
		db.close();
		refreshPersonalInfoList();	// 刷新listView
	}

}
