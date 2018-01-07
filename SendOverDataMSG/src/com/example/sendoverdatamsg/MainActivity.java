package com.example.sendoverdatamsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	
	private Button btnWu;
	private Button btnZhou;
	private Button btnLiu;
	private Button btnYang;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initview();
	}

	private void initview() {
		btnWu = (Button) findViewById(R.id.btn_wuyaochen);
		btnZhou = (Button) findViewById(R.id.btn_zhouyun);
		btnLiu = (Button) findViewById(R.id.btn_liuyulong);
		btnYang = (Button) findViewById(R.id.btn_yangwenming);
		
		btnWu.setOnClickListener(this);
		btnZhou.setOnClickListener(this);
		btnLiu.setOnClickListener(this);
		btnYang.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(MainActivity.this,DataListActivity.class);
		String from = "wuyaochen";
		switch (v.getId()) {
		case R.id.btn_wuyaochen:
			from = "wuyaochen";
			break;
		case R.id.btn_zhouyun:
			from = "zhouyun";
			break;
		case R.id.btn_liuyulong:
			from = "liuyulong";
			break;
		case R.id.btn_yangwenming:
			from = "yangwenming";
			break;

		default:
			break;
		}
		intent.putExtra("from", from);
		startActivity(intent);
	}
}
