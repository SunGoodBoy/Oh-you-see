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
			mProgressDialog.setTitle("Android Jsoup Ecard Web Notification Test");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
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
				Log.d("es class biaotou", es.text());
				for (Element e : es) {
					Elements div = e.getElementsByTag("div");
					String remainText = div.text();
					// 通过指定标签搜索获得通知标题
					for (Element title : div.select("a[style]:gt(0)")) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", title.text());
						Log.d("title", title.text());
						// 通过字符串截取获得发布时间
						remainText = remainText.substring(remainText.indexOf("<") + 1);
						String titleText = remainText.substring(0, remainText.indexOf(">"));
						map.put("publish_time", "发布时间：" + titleText);
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
