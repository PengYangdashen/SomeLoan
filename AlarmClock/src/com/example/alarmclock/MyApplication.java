package com.example.alarmclock;

import android.app.Application;
import android.location.Address;
import android.location.Location;

public class MyApplication extends Application {

	private Application app;
	private static Address address;
	private static Address oldAddress;
	private static Location location;
	private static int count = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public static void setAddress (Address address) {
		MyApplication.address = address;
		MyApplication.oldAddress = MyApplication.address;
	}
	
	public static Address getAddress () {
		return address;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		MyApplication.count = count;
	}
	public static void setCount(boolean b) {
		if (b) {
			count++;
		}
	}

	public static Location getLocation() {
		return location;
	}

	public static void setLocation(Location location) {
		MyApplication.location = location;
	}
	
}
