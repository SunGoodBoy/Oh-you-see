package com.duang.easyecard.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.duang.easyecard.R;

import android.content.ContentUris;
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
	
	private Uri imageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_userpic);
		//ʵ�����ؼ�
		userpicImageView = (ImageView) findViewById(R.id.change_userpic_img);
		takePhotoButton = (Button) findViewById(R.id.change_userpic_take_photo);
		chooseFromAlbumButton = (Button) findViewById(R.id.change_userpic_choose_from_album);
		cancelButton = (Button) findViewById(R.id.change_userpic_cancel);
		
		// ���������ա���ť
		takePhotoButton.setOnClickListener(this);
		
		// �������������ѡȡ����ť
		chooseFromAlbumButton.setOnClickListener(this);
		
		// ������ȡ������ť
		cancelButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		case R.id.change_userpic_choose_from_album:
			Intent intent = new Intent("android.intent.action.GET_CONTENT");
			intent.setType("image/*"); 
			startActivityForResult(intent, GET_ALBUM);
			break;
		case R.id.change_userpic_cancel:
			finish();
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(imageUri));
				userpicImageView.setImageBitmap(bitmap);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case GET_ALBUM:
			if (resultCode == RESULT_OK) {
				// �����ж�ϵͳ�Ƿ�4.4����
				if (Build.VERSION.SDK_INT >= 19) {
					handleImageOnKitKat(data);
				} else {
					handleImageBeforeLitKat(data);
				}
			}
			break;
		default:
			break;
		}
	}

	public void handleImageBeforeLitKat(Intent data) {
		Uri uri = data.getData();
		String imagePath = getImagePath(uri, null);
		showImage(imagePath);
	}

	public void handleImageOnKitKat(Intent data) {
		String imagePath = null;
		Uri uri = data.getData();
		// �����document�͵�uri����ͨ��document id������
		if (DocumentsContract.isDocumentUri(this, uri)) {
			String docid = DocumentsContract.getDocumentId(uri);
			// ���uri��authority������media
			if ("com.android.providers.media.documents".equals(uri
					.getAuthority())) {
				String id = docid.split(":")[0];
				String selection = MediaStore.Images.Media._ID + "=" + id;
				imagePath = getImagePath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
				// // ���uri��authority������downloads
			} else if ("com.android.providers.downloads.documents".equals(uri
					.getAuthority())) {
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(docid));
				imagePath = getImagePath(contentUri, null);
			}
			// �������document���͵�uri����ô����ͨ��������
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {
			imagePath = getImagePath(uri, null);
		}

		showImage(imagePath);
	}

	public String getImagePath(Uri uri, String selection) {
		String imagePath = null;
		Cursor cursor = null;
		cursor = getContentResolver().query(uri, null, selection, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				imagePath = cursor.getString(cursor.getColumnIndex(Media.DATA));
			}
		}
		cursor.close();

		return imagePath;
	}

	public void showImage(String imagePath) {
		if (imagePath != null) {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			userpicImageView.setImageBitmap(bitmap);
		} else {
			Toast.makeText(this, "get image failure", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
}
