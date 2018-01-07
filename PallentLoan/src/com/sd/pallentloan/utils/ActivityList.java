package com.sd.pallentloan.utils;

import java.util.LinkedList;

import android.util.Log;

public class ActivityList {
	private static LinkedList<BaseActivity> list = new LinkedList<BaseActivity>();

	public static void addActiviy(BaseActivity a) {
		if (!list.contains(a)) {
			list.add(a);
		}
	}

	public static BaseActivity getLastActivity() {
		return list.getLast();
	}

	public static void removeActivity(BaseActivity a) {
		if (!list.isEmpty()) {
			list.remove(a);
		}
	}

	/**
	 * 退出，结束程序的所有界面
	 */
	public static void tuichu() {
		int lenth = list.size();
		for (int i = 0; i < lenth; i++) {
			try {
				list.get(i).finish();
			} catch (Exception e) {
			}
		}
		if (list.size()>0) {
			list.clear();
		}
		int l = list.size();
		Log.i("wjj", l + "");
	}

	/**
	 * 
	 * 退出登录，留下一个登录界面
	 * 
	 */
	public static void existLogin() {
		for (int i = 0; i < list.size(); i++) {
			if (i < list.size() - 1) {
				list.get(i).finish();
			}
		}
	}
}
