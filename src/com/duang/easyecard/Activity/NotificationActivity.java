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
import android.content.Intent;
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
	String prefixUrl = "http://ecard.ouc.edu.cn/";
	
	String notificationsPostfixUrl = "xxsearch.action?lmid=5e431e0548c5a86f0148f2e9dec90001";
	String rulesPostfixUrl = "xxsearch.action?lmid=5e431e054b4e1676014b4e24fd7f0002";
	String helpPostfixUrl = "xxsearch.action?lmid=5e431e054b4e1676014b4e24a6d40001";
	String downloadPostfixUrl = "xxsearch.action?lmid=5e431e054b4e1676014b4e29b8500004";
	
	String flag = null;
	String url = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ͨ��flag��ֵ��ȷ����������ƺ�url��ֵ
		Intent intent = getIntent();
		flag = intent.getStringExtra("FLAG");
		if (flag.equals("N")) {
			setTitle("֪ͨ");
			url = prefixUrl + notificationsPostfixUrl;
		}
		if (flag.equals("R")) {
			setTitle("�����ƶ�");
			url = prefixUrl + rulesPostfixUrl;
		}
		if (flag.equals("D")) {
			setTitle("�ļ�����");
			url = prefixUrl + downloadPostfixUrl;
		}
		if (flag.equals("H")) {
			setTitle("У԰��ʹ�ð���");
			url = prefixUrl + helpPostfixUrl;
		}
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
			mProgressDialog.setTitle("��У԰һ��ͨ��վ��ȡ�����Ϣ");
			// Set progressdialog message
			mProgressDialog.setMessage("���ڼ��ء���");
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
				// ����ҳ�н�����֪ͨ����ͷ���ʱ��
				Elements es = doc.getElementsByClass("biaotou");
				String remainText2 = es.toString();
				// Log.d("es class biaotou text", es.text());
				// Log.d("es class biaotou string", remainText2);
				for (Element e : es) {
					Elements div = e.getElementsByTag("div");
					String remainText1 = div.text();
					// ͨ��ָ����ǩ�������֪ͨ����
					for (Element title : div.select("a[style]:gt(0)")) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", title.text());
						// Log.d("title", title.text());
						// ͨ���ַ�����ȡ��÷���ʱ��
						remainText1 = remainText1.substring(remainText1.indexOf("<") + 1);
						String titleText = remainText1.substring(0, remainText1.indexOf(">"));
						map.put("publish_time", "����ʱ�䣺" + titleText);
						// Log.d("publish_time", "����ʱ�䣺" + titleText);
						/*
						 * ����download���͵���ַ������֪ͨ�������������
						 * ������Ҫ�ж��ǲ���download����
						 * Ȼ��ͨ����ͬ���ַ�����ȡ�������Ҫ��ת��ҳ�ĺ�׺
						 */
						if (flag.equals("D")) {
							remainText2 = remainText2.substring(remainText2.indexOf("href=") + 6);
							String postfixUrl = remainText2.substring(0, remainText2.indexOf(">") - 1);
							map.put("postfix_url", postfixUrl);
							// Log.d("postfixUrl", postfixUrl);
							arrayList.add(map);
						} else {
							remainText2 = remainText2.substring(remainText2.indexOf("('") + 2);
							String postfixUrl = remainText2.substring(0, remainText2.indexOf("'"));
							map.put("postfix_url", postfixUrl);
							// Log.d("postfixUrl", postfixUrl);
							arrayList.add(map);
						}
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
