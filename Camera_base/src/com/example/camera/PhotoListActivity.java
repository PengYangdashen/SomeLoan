package com.example.camera;

import java.io.File;
import java.util.ArrayList;

import util.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class PhotoListActivity extends Activity implements OnClickListener{

	private String TAG = getClass().getSimpleName();
	
	private GridView gvPhoto;
	private PhotoWallAdapter adapter;
	private ArrayList<File> list;
	
	private TextView tvBack;
	private TextView tvDocument;
	private TextView tvChoose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photolist);
		String from = getIntent().getStringExtra("From");

		list = new ArrayList<File>();
		// 创建一个文件夹对象，赋值为外部存储器的目录
		File sdcardDir = Environment.getExternalStorageDirectory();
		// 得到一个路径，内容是sdcard的文件夹路径和名字
		String path = sdcardDir.getPath() + "/" + from;
		File pictureDir = new File(path);
		if (!pictureDir.exists()) {
			// 若不存在，创建目录，可以在应用启动的时候创建
			pictureDir.mkdirs();
		}
		getAllFiles(pictureDir);
		
		initview();
		tvDocument.setText(from);
	}

	private void initview() {
		// TODO Auto-generated method stub
		tvBack = (TextView) findViewById(R.id.tv_back);
		tvBack.setOnClickListener(this);
		tvDocument = (TextView) findViewById(R.id.tv_document);
		tvDocument.setOnClickListener(this);
		tvChoose = (TextView) findViewById(R.id.tv_choose);
		tvChoose.setOnClickListener(this);
		
		gvPhoto = (GridView) findViewById(R.id.gv_photo);
		adapter = new PhotoWallAdapter(this, 0, list, gvPhoto);
		gvPhoto.setAdapter(adapter);
	}

	/**
	 * 获得指定目录下图片文件
	 */
	private void getAllFiles(File root) {
		LogUtil.d(TAG, root.getName());
		File files[] = root.listFiles();
		if (files != null)
			for (File f : files) {
				LogUtil.d(TAG, f.getName());
				if (f.isDirectory()) {
					LogUtil.d(TAG, "getAllFiles --> f.isDirectory():" + f.isDirectory());
					getAllFiles(f);
				} else {
					LogUtil.d(TAG, "getAllFiles --> f.isDirectory():" + f.isDirectory());
					if (f.getName().endsWith(".png")
							|| f.getName().endsWith(".jpg")
							|| f.getName().endsWith(".jpeg"))
						this.list.add(f);
				}
			}
	}

	protected void onDestroy() {
		super.onDestroy();
		adapter.cancelAllTasks();// 退出程序时结束所有的下载任务
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;

		case R.id.tv_choose:
			break;
			
		default:
			break;
		}
	}

}
