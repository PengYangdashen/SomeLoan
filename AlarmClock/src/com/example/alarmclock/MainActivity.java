package com.example.alarmclock;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private String TAG = getClass().getSimpleName();
	private int mHour = -1;
	private int mMinute = -1;

	public static final long DAY = 1000L * 60 * 60 * 24;
	
	private TextView tvCount;
	private TextView tvLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvCount = (TextView) findViewById(R.id.tv_count);
		tvLocation = (TextView) findViewById(R.id.tv_location);
		Log.w(TAG, "into onCreate");
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		Log.w(TAG, "sp.getString(\"isSet\", \"null\") ---> " + sp.getString("isSet", null));
		if (sp.getString("isSet", null) == null) {
			Editor editor = sp.edit();
//			setNormalAlarm();
			setRepeatAlarm();
			editor.putString("isSet", "seted");
			editor.commit(); 
			Log.w(TAG, "alarm is seted just now");
		} else if ("seted".equalsIgnoreCase(sp.getString("isSet", null))){
			Log.w(TAG, "alarm was alreadly seted");
		}
	}
	
	private void setNormalAlarm () {
		Intent intent = new Intent(MainActivity.this, AlarmReceiver2.class);
		PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

		// 过10s 执行这个闹铃
		Calendar calendar = Calendar.getInstance();
	 	calendar.setTimeInMillis(System.currentTimeMillis());
	 	calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		calendar.add(Calendar.SECOND, 10);

		// 进行闹铃注册
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		
		Toast.makeText(MainActivity.this, "设置简单闹铃成功!", Toast.LENGTH_LONG).show();
	}
	
	private void setRepeatAlarm () {


		Intent intent = new Intent(MainActivity.this, AlarmReceiver2.class);
		PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();	// 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
	 	calendar.setTimeInMillis(System.currentTimeMillis());
	 	calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
	 	calendar.set(Calendar.MINUTE, 10);
	 	calendar.set(Calendar.HOUR_OF_DAY, 18);
	 	calendar.set(Calendar.SECOND, 0);
	 	calendar.set(Calendar.MILLISECOND, 0);

	 	// 选择的每天定时时间
	 	long selectTime = calendar.getTimeInMillis();	

	 	// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
	 	if(systemTime > selectTime) {
	 		Toast.makeText(MainActivity.this, "设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
//	 		calendar.add(Calendar.DAY_OF_MONTH, 1);
	 		calendar.add(Calendar.HOUR_OF_DAY, 1);
	 		selectTime = calendar.getTimeInMillis();
	 	}

	 	// 计算现在时间到设定时间的时间差
	 	long time = selectTime - systemTime;
 		firstTime += time;

        // 进行闹铃注册
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        firstTime, 3600*1000, sender);

        Log.i(TAG, "time ==== " + time + ", selectTime ===== "
    			+ selectTime + ", systemTime ==== " + systemTime + ", firstTime === " + firstTime);

        Toast.makeText(MainActivity.this, "设置重复闹铃成功! ", Toast.LENGTH_LONG).show();
	
	}

	@Override
	protected void onResume() {
		Address address = MyApplication.getAddress();
		Location location = MyApplication.getLocation();
		int count = MyApplication.getCount();
		// 获取纬度
		double lat = location.getLatitude();
		// 获取经度
		double lon = location.getLongitude();
		// 位置提供者
		String provider = location.getProvider();
		// 位置的准确性
		float accuracy = location.getAccuracy();
		// 高度信息
		double altitude = location.getAltitude();
		// 方向角
		float bearing = location.getBearing();
		// 速度 米/秒
		float speed = location.getSpeed();
		
		String city = address.getLocality();
		String state = address.getAdminArea();
		String zipCode = address.getPostalCode();
		String country = address.getCountryCode();
		
		tvLocation.setText("经度：" + lon + "\n纬度：" + lat + "\n服务商：" + provider
		 + "\n准确性：" + accuracy + "\n高度：" + altitude + "\n方向角："
		 + bearing + "\n速度：" + speed +  "\n您所在的城市："
		 + address.getCountryName()
			+ address.getLocality() + address.getSubThoroughfare());
		
		tvLocation.append("\n " + address.getAddressLine(0) + "," + city + "," + state + ","
		 + zipCode + "," + country);
		
		tvCount.setText("" + count);
		super.onResume();
	}
}
