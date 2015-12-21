package com.duang.ohyousee.Activity;

import com.duang.ohyousee.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class AppStart extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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