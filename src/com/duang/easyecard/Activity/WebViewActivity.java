package com.duang.easyecard.Activity;

import com.duang.easyecard.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends BaseActivity {

	private WebView webView;
	// 指定网址的前缀
	private  final String prefixUrl = "http://ecard.ouc.edu.cn/";
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
		
		// 监听下载
		webView.setDownloadListener(new DownloadListener() {
			
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				// 当用户点击下载链接时，直接调用系统的浏览器下载
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
                startActivity(intent);
			}
		});
		// 设置支持缩放
		webView.getSettings().setBuiltInZoomControls(true);
		
		Intent intent = getIntent();
		String Url = prefixUrl + intent.getStringExtra("postfixUrl");
		webView.loadUrl(Url);
	}
}
