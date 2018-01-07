package com.sd.pallentloan.utils;

import com.moxie.client.manager.MoxieSDK;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class BaseApplication extends Application {
	private static Context mContext;
	private boolean isDownload;
	private static BaseApplication mApplication;

	public synchronized static BaseApplication getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		// ShareSDK.initSDK(this);

		Log.e("eeee", "进来的次数");
		// 是否开启debug模式
		mContext = this;
		isDownload = false;
		mApplication = this;
		super.onCreate();
		MoxieSDK.init(this);
		// TelephonyManager tm = (TelephonyManager) this
		// .getSystemService(Context.TELEPHONY_SERVICE);
		// String deviceId = tm.getDeviceId();

	}

	public static Context getAppContext() {
		return mContext;
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

}
