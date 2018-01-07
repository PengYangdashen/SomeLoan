package com.sd.pallentloan;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.pallentloan.R;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.HttpUtils;
import com.sd.pallentloan.view.MyProgressDialog;
public class ProblemActivity extends BaseActivity implements OnClickListener {

	private String url = "http://www.lvzbao.com/androidHtml/gsdcjwt_app.html";
	private WebSettings webSettings;
	private WebView home_webview;
	private MyProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_problem);
		initView();
	}

	private void initView() {
		dialog = new MyProgressDialog(ProblemActivity.this, "");
		dialog.show();
		Intent intent = getIntent();
		 url = intent.getStringExtra("url");
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText(intent.getStringExtra("title"));
		
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);

		home_webview = (WebView) findViewById(R.id.webview);
		webSettings = home_webview.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		home_webview.setScrollBarStyle(0);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		home_webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
				if (progress == 100) {
					dialog.dismiss();
				}
				super.onProgressChanged(view, progress);
			}
		});

		home_webview.loadUrl(url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

}
