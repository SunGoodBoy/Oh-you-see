package com.duang.easyecard.Activity;

import com.duang.easyecard.R;
import com.duang.easyecard.R.layout;
import com.duang.easyecard.db.MyDatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class AppStart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				Intent intent = new Intent (AppStart.this,LoginActivity.class);			
				startActivity(intent);
				AppStart.this.finish();
			}
		}, 1000);
	}

}