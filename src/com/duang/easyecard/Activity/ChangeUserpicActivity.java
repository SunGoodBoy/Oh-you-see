package com.duang.easyecard.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.duang.easyecard.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ChangeUserpicActivity extends BaseActivity {

	private ImageView mUserpic;
	private Button takePhotoButton;
	private Button chooseFromAlbumButton;
	private Button cancelButton;
	
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	
	private Uri imageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_userpic);
		//实例化控件
		mUserpic = (ImageView) findViewById(R.id.change_userpic_img);
		takePhotoButton = (Button) findViewById(R.id.change_userpic_take_photo);
		chooseFromAlbumButton = (Button) findViewById(R.id.change_userpic_choose_from_album);
		cancelButton = (Button) findViewById(R.id.change_userpic_cancel);
		
		// 设置“拍照”按钮的点击事件
		takePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 创建File对象，用于存储拍照后的图片
				File outputImage = new File(Environment.getExternalStorageDirectory(),
						"output_image.jpg");
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
				startActivityForResult(intent, TAKE_PHOTO);  //启动相机程序
			}
		});
		
		// 设置“从相册中选取”按钮的点击事件
		chooseFromAlbumButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 创建File对象，用于存储拍照后的图片
				File outputImage = new File(Environment.getExternalStorageDirectory(),
						"output_image.jpg");
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent("android.intent.action.GET_CONTENT");
				intent.setType("image/*");
				intent.putExtra("crop", true);
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
		});
		
		// 设置取消按钮的点击事件
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					Bitmap bitmap = BitmapFactory.decodeStream
							(getContentResolver().openInputStream(imageUri));
					mUserpic.setImageBitmap(bitmap);  //将裁剪后的照片显示出来
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}
}
