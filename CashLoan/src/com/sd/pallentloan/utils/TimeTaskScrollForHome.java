package com.sd.pallentloan.utils;

import java.util.ArrayList;
import java.util.TimerTask;

import com.sd.pallentloan.adapter.HomeAdapterForHome;
import com.sd.pallentloan.pojo.PersonForHome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class TimeTaskScrollForHome extends TimerTask {

	private ListView listView;

	public TimeTaskScrollForHome(Context context, ListView listView,
			ArrayList<PersonForHome> pojo) {
		this.listView = listView;
		listView.setAdapter(new HomeAdapterForHome(context, pojo));
	}

	private Handler handler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			listView.smoothScrollBy(1, 0);
		};
	};

	@Override
	public void run() {
		Message msg = handler.obtainMessage();
		handler.sendMessage(msg);
	}

}
