package com.duang.easyecard.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.duang.easyecard.R;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ChangeUserpicActivity extends BaseActivity implements OnClickListener{

	private ImageView userpicImageView;
	private Button takePhotoButton;
	private Button chooseFromAlbumButton;
	private Button cancelButton;
	
	private static final int TAKE_PHOTO = 1;
	private static final int CROP_PHOTO = 2;
	private static final int GET_ALBUM = 3;
	
	private static final int SCALE = 5;  // 照片缩小比例
	
	private Uri imageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_userpic);
		// 实例化控件
		userpicImageView = (ImageView) findViewById(R.id.change_userpic_img);
		takePhotoButton = (Button) findViewById(R.id.change_userpic_take_photo);
		chooseFromAlbumButton = (Button) findViewById(R.id.change_userpic_choose_from_album);
		cancelButton = (Button) findViewById(R.id.change_userpic_cancel);
		
		// 监听“拍照”按钮
		takePhotoButton.setOnClickListener(this);
		
		// 监听“从相册中选取”按钮
		chooseFromAlbumButton.setOnClickListener(this);
		
		// 监听“取消”按钮
		cancelButton.setOnClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 
		case R.id.change_userpic_take_photo:
			File image = new File(Environment.getExternalStorageDirectory(),
					"image.jpg");
			if (image.exists()) {
				image.delete();
			}
			try {
				image.createNewFile();
				imageUri = Uri.fromFile(image);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKE_PHOTO);

			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		// 
		case R.id.change_userpic_choose_from_album:
			Intent intent = new Intent("android.intent.action.GET_CONTENT");
			intent.setType("image/*"); 
			startActivityForResult(intent, GET_ALBUM);
			break;
		// 
		case R.id.change_userpic_cancel:
			finish();
		default:
			break;
		}

	}
	
	// 裁剪图片后显示
	public void showPicturePicker(Context context) {
		
	}
}
