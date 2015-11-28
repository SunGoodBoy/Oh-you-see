package com.duang.easyecard.Activity;

import com.duang.easyecard.R;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends BaseActivity {

	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view);
		
		webView = (WebView) findViewById(R.id.web_view);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String
					url) {
				view.loadUrl(url);  //根据传入的参数加载网页
				return true;
			}
		});
		Intent intent = getIntent();
		String Url = intent.getStringExtra("IndexUrl");
		webView.loadUrl(Url);
	}
}
