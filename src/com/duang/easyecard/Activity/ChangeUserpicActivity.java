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
import android.util.Log;
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
	public static final int GET_PHOTO_FROM_ALBUM = 2;
	public static final int CROP_PHOTO = 3;
	
	private Uri imageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_userpic);
		//ʵ�����ؼ�
		mUserpic = (ImageView) findViewById(R.id.change_userpic_img);
		takePhotoButton = (Button) findViewById(R.id.change_userpic_take_photo);
		chooseFromAlbumButton = (Button) findViewById(R.id.change_userpic_choose_from_album);
		cancelButton = (Button) findViewById(R.id.change_userpic_cancel);
		
		// ���á����ա���ť�ĵ���¼�
		takePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ����File�������ڴ洢���պ��ͼƬ
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
				startActivityForResult(intent, TAKE_PHOTO);  //�����������
			}
		});
		
		// ���á��������ѡȡ����ť�ĵ���¼�
		chooseFromAlbumButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ����File�������ڴ洢ѡ���ͼƬ
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
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				intent.putExtra("crop", true);
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, GET_PHOTO_FROM_ALBUM);
			}
		});
		
		// ����ȡ����ť�ĵ���¼�
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
				if (data != null) {
					Uri imageUri = data.getData();
					cropPhoto(imageUri);
				}
			}
			break;
		case GET_PHOTO_FROM_ALBUM:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Uri imageUri = data.getData();
				}
			}
		
		default:
			break;
		}
	}

	private void cropPhoto(Uri imageUri) {
		// �ü�ͼƬ��ͼ
		Intent intent = new Intent("com.android.camera.aciton.CROP");
		intent.setDataAndType(imageUri, "image/*");
		intent.putExtra("crop", true);
		// �ü���ı�����1��1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// �ü������ͼƬ�ĳߴ��С
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);

		intent.putExtra("outputFormat", "JPEG");// ͼƬ��ʽ
		intent.putExtra("noFaceDetection", true);// ȡ������ʶ��
		intent.putExtra("return-data", true);
		
		// ����һ�����з���ֵ��Activity��������ΪCROP_PHOTO
		startActivityForResult(intent, CROP_PHOTO);
	}
}
