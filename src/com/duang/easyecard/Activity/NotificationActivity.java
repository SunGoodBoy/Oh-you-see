package com.duang.easyecard.Activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.duang.easyecard.R;
import com.duang.easyecard.util.NotificationListViewAdapter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class NotificationActivity extends BaseActivity {

	ListView listView;
	NotificationListViewAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arrayList;
	
	public static String TITLE = "title";
	public static String PUBLISH_TIME = "publish_time";
	public static String POSTFIX_URL = "postfix_url";
	
	// URL Adress
	String url = "http://ecard.ouc.edu.cn/xxsearch.action?lmid=5e431e0548c5a86f0148f2e9dec90001";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_list_view);
		
		// Execute DownloadJSON AsyncTask
		new JsoupListView().execute();
	}
	
	
	// Title AsyncTask
	private class JsoupListView extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			Log.d("JsoupListView AsyncTask", "OnPreExecute.");
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(NotificationActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("从一卡通网站获取通知");
			// Set progressdialog message
			mProgressDialog.setMessage("正在加载……");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.d("JsoupListView AsyncTask", "doInBackground.");
			// Create an array
			arrayList = new ArrayList<HashMap<String, String>>();
			
			Document doc = null;
			
			try {
				doc = Jsoup.parse(new URL(url), 5000);
				// 在网页中解析出通知标题和发布时间
				Elements es = doc.getElementsByClass("biaotou");
				String remainText2 = es.toString();
				// Log.d("es class biaotou text", es.text());
				// Log.d("es class biaotou string", remainText2);
				for (Element e : es) {
					Elements div = e.getElementsByTag("div");
					String remainText1 = div.text();
					// 通过指定标签搜索获得通知标题
					for (Element title : div.select("a[style]:gt(0)")) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", title.text());
						// Log.d("title", title.text());
						// 通过字符串截取获得发布时间
						remainText1 = remainText1.substring(remainText1.indexOf("<") + 1);
						String titleText = remainText1.substring(0, remainText1.indexOf(">"));
						map.put("publish_time", "发布时间：" + titleText);
						// Log.d("publish_time", "发布时间：" + titleText);
						// 通过字符串截取获得要跳转网页的后缀
						remainText2 = remainText2.substring(remainText2.indexOf("('") + 2);
						String postfixUrl = remainText2.substring(0, remainText2.indexOf("'"));
						map.put("postfix_url", postfixUrl);
						// Log.d("postfixUrl", postfixUrl);
						arrayList.add(map);
					}
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// Locate the listview in listview_main.xml
			listView = (ListView) findViewById(R.id.notification_list_view);
			// Pass the results into ListViewAdapter.java
			adapter = new NotificationListViewAdapter(NotificationActivity.this, arrayList);
			// Set the adapter to the ListView
			listView.setAdapter(adapter);
			// Close the progressdialog
			mProgressDialog.dismiss();
		}
	}
}
