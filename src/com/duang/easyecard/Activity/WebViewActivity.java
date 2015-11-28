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
	// ָ����ַ��ǰ׺
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
				view.loadUrl(url);  //���ݴ���Ĳ���������ҳ
				return true;
			}
		});
		
		// ��������
		webView.setDownloadListener(new DownloadListener() {
			
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				// ���û������������ʱ��ֱ�ӵ���ϵͳ�����������
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
                startActivity(intent);
			}
		});
		// ����֧������
		webView.getSettings().setBuiltInZoomControls(true);
		
		Intent intent = getIntent();
		String Url = prefixUrl + intent.getStringExtra("postfixUrl");
		webView.loadUrl(Url);
	}
}